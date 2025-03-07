/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import eu.kennytv.maintenance.api.Maintenance;
import eu.kennytv.maintenance.api.MaintenanceProvider;
import eu.kennytv.maintenance.api.event.MaintenanceChangedEvent;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.command.MaintenanceCommand;
import eu.kennytv.maintenance.core.dump.MaintenanceDump;
import eu.kennytv.maintenance.core.dump.PluginDump;
import eu.kennytv.maintenance.core.event.EventManager;
import eu.kennytv.maintenance.core.hook.ServerListPlusHook;
import eu.kennytv.maintenance.core.runnable.MaintenanceRunnable;
import eu.kennytv.maintenance.core.runnable.MaintenanceScheduleRunnable;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.core.util.ServerType;
import eu.kennytv.maintenance.core.util.Task;
import eu.kennytv.maintenance.core.util.Version;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.NamedTextColor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public abstract class MaintenancePlugin
implements Maintenance {
    public static final Gson GSON = new GsonBuilder().create();
    public static final String HANGAR_URL = "https://hangar.papermc.io/kennytv/Maintenance";
    private static final Pattern INT_PATTERN = Pattern.compile("[0-9]+");
    protected final eu.kennytv.maintenance.api.event.manager.EventManager eventManager;
    protected final Version version;
    protected Settings settings;
    protected ServerListPlusHook serverListPlusHook;
    protected MaintenanceRunnable runnable;
    protected MaintenanceCommand commandManager;
    private final Component prefix;
    private final ServerType serverType;
    private Version newestVersion;
    private boolean debug;

    protected MaintenancePlugin(String version, ServerType serverType) {
        this.version = new Version(version);
        this.serverType = serverType;
        this.prefix = ((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)Component.text().append((ComponentBuilder<?, ?>)Component.text().content("[").color(NamedTextColor.DARK_GRAY))).append((ComponentBuilder<?, ?>)Component.text().content("Maintenance").color(NamedTextColor.YELLOW))).append((ComponentBuilder<?, ?>)Component.text().content("]").color(NamedTextColor.DARK_GRAY))).append((Component)Component.text(" "))).build();
        this.eventManager = new EventManager();
        MaintenanceProvider.setMaintenance(this);
    }

    public void disable() {
    }

    @Override
    public void setMaintenance(boolean maintenance) {
        this.settings.setMaintenance(maintenance);
        this.settings.getConfig().set("maintenance-enabled", maintenance);
        this.settings.saveConfig();
        this.serverActions(maintenance);
        for (String command : maintenance ? this.settings.getCommandsOnMaintenanceEnable() : this.settings.getCommandsOnMaintenanceDisable()) {
            try {
                this.executeConsoleCommand(command);
            } catch (Exception e) {
                this.getLogger().log(Level.SEVERE, "Error while executing extra maintenance " + (maintenance ? "enable" : "disable") + " command: " + command, e);
            }
        }
    }

    public void serverActions(boolean maintenance) {
        if (this.isTaskRunning()) {
            this.cancelTask();
        }
        if (this.serverListPlusHook != null && this.settings.isEnablePingMessages()) {
            this.serverListPlusHook.setEnabled(!maintenance);
        }
        if (maintenance) {
            this.broadcast(this.settings.getMessage("maintenanceActivated", new String[0]));
            if (this.settings.isKickOnlinePlayers()) {
                this.kickPlayers();
            }
        } else {
            this.broadcast(this.settings.getMessage("maintenanceDeactivated", new String[0]));
        }
        this.eventManager.callEvent(new MaintenanceChangedEvent(maintenance));
    }

    public String replacePingVariables(String component) {
        if (component.contains("%TIMER%")) {
            component = component.replace("%TIMER%", this.getTimerMessage());
        }
        component = component.replace("%ONLINE%", String.valueOf(this.getOnlinePlayers()));
        component = component.replace("%MAX%", String.valueOf(this.getMaxPlayers()));
        return component;
    }

    public String getTimerMessage() {
        if (!this.isTaskRunning()) {
            return this.settings.getLanguageString("motdTimerNotRunning", new String[0]);
        }
        int preHours = this.runnable.getSecondsLeft() / 60;
        int minutes = preHours % 60;
        int seconds = this.runnable.getSecondsLeft() % 60;
        return this.settings.getLanguageString("motdTimer", "%HOURS%", String.format("%02d", preHours / 60), "%MINUTES%", String.format("%02d", minutes), "%SECONDS%", String.format("%02d", seconds));
    }

    public String getFormattedTime(int timeSeconds) {
        int preHours = timeSeconds / 60;
        int minutes = preHours % 60;
        int seconds = timeSeconds % 60;
        StringBuilder buider = new StringBuilder();
        this.append(buider, "hour", preHours / 60);
        this.append(buider, "minute", minutes);
        this.append(buider, "second", seconds);
        return buider.toString();
    }

    private void append(StringBuilder builder, String timeUnit, int time) {
        if (time == 0) {
            return;
        }
        if (builder.length() != 0) {
            builder.append(' ');
        }
        builder.append(time).append(' ').append(this.settings.language.getString(time == 1 ? timeUnit : timeUnit + "s"));
    }

    public void startMaintenanceRunnable(Duration duration, boolean enable) {
        this.runnable = new MaintenanceRunnable(this, this.settings, (int)duration.getSeconds(), enable);
        if (this.settings.isSaveEndtimerOnStop() && !this.runnable.shouldEnable()) {
            this.settings.setSavedEndtimer(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(this.runnable.getSecondsLeft()));
        }
    }

    public void scheduleMaintenanceRunnable(Duration enableIn, Duration maintenanceDuration) {
        this.runnable = new MaintenanceScheduleRunnable(this, this.settings, (int)enableIn.getSeconds(), (int)maintenanceDuration.getSeconds());
    }

    public boolean updateAvailable() {
        try {
            this.checkNewestVersion();
            return this.version.compareTo(this.newestVersion) < 0;
        } catch (Exception e) {
            return false;
        }
    }

    protected void continueLastEndtimer() {
        if (!this.settings.isSaveEndtimerOnStop()) {
            return;
        }
        if (this.settings.getSavedEndtimer() == 0L) {
            return;
        }
        long current = System.currentTimeMillis();
        this.getLogger().info("Found interrupted endtimer from last uptime...");
        if (!this.isMaintenance()) {
            this.getLogger().info("Maintenance has already been disabled, thus the timer has been cancelled.");
            this.settings.setSavedEndtimer(0L);
        } else if (this.settings.getSavedEndtimer() < current) {
            this.getLogger().info("The endtimer has already expired, maintenance has been disabled.");
            this.setMaintenance(false);
            this.settings.setSavedEndtimer(0L);
        } else {
            this.startMaintenanceRunnable(Duration.ofMillis(this.settings.getSavedEndtimer() - current), false);
            this.getLogger().info("The timer has been continued - maintenance will be disabled in: " + this.getTimerMessage());
        }
    }

    protected void sendEnableMessage() {
        if (!this.settings.hasUpdateChecks()) {
            return;
        }
        this.async(() -> {
            try {
                this.checkNewestVersion();
            } catch (Exception e) {
                return;
            }
            int compare = this.version.compareTo(this.newestVersion);
            if (compare < 0) {
                this.getLogger().warning("Newest version available: Version " + this.newestVersion + ", you're on " + this.version);
            } else if (compare > 0) {
                if (this.version.getTag().equalsIgnoreCase("snapshot")) {
                    this.getLogger().info("You're running a development version, please report bugs on the Discord server (https://discord.gg/vGCUzHq) or the GitHub issue tracker (https://github.com/kennytv/Maintenance/issues)");
                } else {
                    this.getLogger().info("You're running a version, that doesn't exist!");
                }
            }
        });
    }

    public boolean installUpdate() throws Exception {
        String platformInfix = this.serverType == ServerType.VELOCITY ? "Velocity-" : (this.serverType == ServerType.SPONGE ? "Sponge-" : "");
        String fileName = "Maintenance-" + platformInfix + this.newestVersion + ".jar";
        Path tempFilePath = Paths.get(this.getPluginFolder() + "Maintenance.tmp", new String[0]);
        URLConnection connection = new URL("https://github.com/kennytv/Maintenance/releases/download/" + this.newestVersion + "/" + fileName).openConnection();
        try (BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
             BufferedOutputStream os = new BufferedOutputStream(Files.newOutputStream(tempFilePath, new OpenOption[0]));){
            this.writeFile(is, os);
        }
        File file = tempFilePath.toFile();
        long newlength = file.length();
        if (newlength < 10000L) {
            file.delete();
            return false;
        }
        try (InputStream is = Files.newInputStream(file.toPath(), new OpenOption[0]);
             BufferedOutputStream os = new BufferedOutputStream(Files.newOutputStream(this.getPluginFile().toPath(), new OpenOption[0]));){
            this.writeFile(is, os);
        }
        file.delete();
        return true;
    }

    private void writeFile(InputStream is, OutputStream os) throws IOException {
        int chunkSize;
        byte[] chunk = new byte[1024];
        while ((chunkSize = is.read(chunk)) != -1) {
            os.write(chunk, 0, chunkSize);
        }
    }

    private void checkNewestVersion() throws Exception {
        URLConnection connection = new URL("https://hangar.papermc.io/api/v1/projects/Maintenance/latestrelease").openConnection();
        connection.setRequestProperty("User-Agent", "Maintenance/" + this.getVersion());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));){
            String newVersionString = reader.readLine();
            Version newVersion = new Version(newVersionString);
            if (!newVersion.equals(this.version)) {
                this.newestVersion = newVersion;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String pasteDump() {
        MaintenanceDump dump = new MaintenanceDump(this, this.settings);
        try {
            HttpURLConnection connection = (HttpURLConnection)new URL("https://api.pastes.dev/post").openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Maintenance/" + this.getVersion());
            connection.setRequestProperty("Content-Type", "text/plain");
            try (OutputStream out = connection.getOutputStream();){
                out.write(new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson((Object)dump).getBytes(StandardCharsets.UTF_8));
            }
            if (connection.getResponseCode() == 503) {
                this.getLogger().warning("Could not paste dump, pastes.dev down?");
                return null;
            }
            throwable = null;
            try (InputStream in = connection.getInputStream();){
                String output = CharStreams.toString(new InputStreamReader(in));
                JsonObject jsonOutput = (JsonObject)GSON.fromJson(output, JsonObject.class);
                if (!jsonOutput.has("key")) {
                    this.getLogger().log(Level.WARNING, "Could not paste dump, there was no key returned :(");
                    String string2 = null;
                    return string2;
                }
                String string = jsonOutput.get("key").getAsString();
                return string;
            } catch (Throwable throwable5) {
                throwable = throwable5;
                throw throwable5;
            }
        } catch (IOException e) {
            this.getLogger().log(Level.WARNING, "Could not paste dump :(", e);
            return null;
        }
    }

    public void loadMaintenanceIcon() {
        File file = new File(this.getDataFolder(), "maintenance-icon.png");
        if (!file.exists()) {
            this.getLogger().warning("Could not find a 'maintenance-icon.png' file - did you create one in the plugin's folder?");
            return;
        }
        try {
            this.loadIcon(file);
        } catch (Exception e) {
            this.getLogger().log(Level.WARNING, "Could not load the 'maintenance-icon.png' file!");
            e.printStackTrace();
        }
    }

    public void cancelTask() {
        if (this.settings.isSaveEndtimerOnStop() && !this.runnable.shouldEnable()) {
            this.settings.setSavedEndtimer(0L);
        }
        this.runnable.getTask().cancel();
        this.runnable = null;
    }

    @Nullable
    public UUID checkUUID(SenderInfo sender, String s) {
        UUID uuid;
        try {
            uuid = UUID.fromString(s);
        } catch (Exception e) {
            sender.send(this.settings.getMessage("invalidUuid", new String[0]));
            return null;
        }
        return uuid;
    }

    public String[] removeArrayIndex(String[] args, int index) {
        ArrayList<String> argsList = Lists.newArrayList(args);
        argsList.remove(index);
        return argsList.toArray(new String[0]);
    }

    public boolean isNumeric(String string) {
        return INT_PATTERN.matcher(string).matches();
    }

    @Override
    public boolean isMaintenance() {
        return this.settings.isMaintenance();
    }

    @Override
    public boolean isTaskRunning() {
        return this.runnable != null;
    }

    @Override
    public Settings getSettings() {
        return this.settings;
    }

    @Override
    public eu.kennytv.maintenance.api.event.manager.EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public String getVersion() {
        return this.version.toString();
    }

    @Nullable
    public List<String> getMaintenanceServersDump() {
        return this.isMaintenance() ? Arrays.asList("global") : null;
    }

    @Override
    public boolean isDebug() {
        return this.debug;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getSaltLevel() {
        return Integer.MAX_VALUE;
    }

    public Version getNewestVersion() {
        return this.newestVersion;
    }

    public Component prefix() {
        return this.prefix;
    }

    @Nullable
    public MaintenanceRunnable getRunnable() {
        return this.runnable;
    }

    public MaintenanceCommand getCommandManager() {
        return this.commandManager;
    }

    public ServerType getServerType() {
        return this.serverType;
    }

    protected String getPluginFolder() {
        return "plugins/";
    }

    public abstract void async(Runnable var1);

    protected abstract void executeConsoleCommand(String var1);

    public abstract void broadcast(Component var1);

    public abstract Task startMaintenanceRunnable(Runnable var1);

    public abstract CompletableFuture<@Nullable SenderInfo> getOfflinePlayer(String var1);

    public abstract CompletableFuture<@Nullable SenderInfo> getOfflinePlayer(UUID var1);

    public abstract File getDataFolder();

    @Nullable
    public InputStream getResource(String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }

    public abstract Logger getLogger();

    public abstract String getServerVersion();

    public abstract List<PluginDump> getPlugins();

    protected abstract void loadIcon(File var1) throws Exception;

    protected abstract void kickPlayers();

    protected abstract File getPluginFile();

    protected abstract int getOnlinePlayers();

    protected abstract int getMaxPlayers();
}

