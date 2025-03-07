/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core;

import eu.kennytv.maintenance.api.event.MaintenanceReloadedEvent;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.config.Config;
import eu.kennytv.maintenance.core.config.ConfigSection;
import eu.kennytv.maintenance.core.util.ServerType;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.MiniMessage;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.jetbrains.annotations.Nullable;

public class Settings
implements eu.kennytv.maintenance.api.Settings {
    public static final String NEW_LINE_REPLACEMENT = "<br>";
    private static final int CONFIG_VERSION = 9;
    private static final int LANGUAGE_VERSION = 2;
    private static final Random RANDOM = new Random();
    protected final MaintenancePlugin plugin;
    private final Map<UUID, String> whitelistedPlayers = new HashMap<UUID, String>();
    private final String[] unsupportedFields;
    protected boolean maintenance;
    private Set<Integer> broadcastIntervals;
    private List<String> pingMessages;
    private List<String> timerSpecificPingMessages;
    private List<String> commandsOnMaintenanceEnable;
    private List<String> commandsOnMaintenanceDisable;
    private String legacyParsedPlayerCountMessage;
    private String legacyParsedTimerPlayerCountMessage;
    private List<String> legacyParsedPlayerCountHoverLines;
    private List<String> legacyParsedTimerPlayerCountHoverLines;
    private String prefixString;
    private String languageName;
    private boolean enablePingMessages;
    private boolean customPlayerCountMessage;
    private boolean customPlayerCountHoverMessage;
    private boolean customMaintenanceIcon;
    private boolean joinNotifications;
    private boolean updateChecks;
    private boolean saveEndtimerOnStop;
    private boolean kickOnlinePlayers;
    private boolean debug;
    private long savedEndtimer;
    protected Config config;
    protected Config language;
    protected Config whitelist;

    public Settings(MaintenancePlugin plugin, String ... unsupportedFields) {
        this.plugin = plugin;
        this.unsupportedFields = unsupportedFields;
        if (!plugin.getDataFolder().exists()) {
            this.updatePluginDirectory();
            plugin.getDataFolder().mkdirs();
        }
        this.createFile("config.yml");
        this.createFile("WhitelistedPlayers.yml");
        this.reloadConfigs();
    }

    @Override
    public void reloadConfigs() {
        try {
            this.config = new Config(new File(this.plugin.getDataFolder(), "config.yml"), this.unsupportedFields);
            this.config.load();
            this.config.resetAwesomeHeader();
            this.whitelist = new Config(new File(this.plugin.getDataFolder(), "WhitelistedPlayers.yml"), new String[0]);
            this.whitelist.load();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load Maintenance files - probably a malformed config file", e);
        }
        this.loadSettings();
        this.createLanguageFile();
        try {
            this.language = new Config(new File(this.plugin.getDataFolder(), "language-" + this.languageName + ".yml"), new String[0]);
            this.language.load();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load Maintenance language file - probably a malformed file", e);
        }
        try {
            this.updateLanguageFile();
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Couldn't update language file", e);
        }
        this.prefixString = this.language.getString("prefix");
        this.plugin.getEventManager().callEvent(new MaintenanceReloadedEvent());
    }

    public void saveConfig() {
        try {
            this.config.save();
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Couldn't save the config file!", e);
        }
    }

    public void createFile(String name) {
        this.createFile(name, name);
    }

    private void createFile(String name, String from) {
        File file = new File(this.plugin.getDataFolder(), name);
        if (!file.exists()) {
            try (InputStream in = this.plugin.getResource(from);){
                Files.copy(in, file.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create " + name + " file for Maintenance!", e);
            }
        }
    }

    private void saveWhitelistedPlayers() {
        try {
            this.whitelist.save();
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Couldn't save the whitelisted players file!", e);
        }
    }

    private void createLanguageFile() {
        block15: {
            String fileName = "language-" + this.languageName + ".yml";
            File file = new File(this.plugin.getDataFolder(), fileName);
            if (file.exists()) {
                return;
            }
            try (InputStream in = this.plugin.getResource(fileName);){
                Files.copy(in, file.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                this.plugin.getLogger().warning("Unable to provide language " + this.languageName);
                if (this.languageName.equals("en")) break block15;
                this.plugin.getLogger().warning("Falling back to default language: en");
                this.languageName = "en";
                this.createLanguageFile();
            }
        }
    }

    private void loadSettings() {
        this.updateConfig();
        ConfigSection pingMessageSection = this.config.getSection("ping-message");
        this.enablePingMessages = pingMessageSection.getBoolean("enabled", true);
        this.pingMessages = this.loadPingMessages(pingMessageSection.getStringList("messages"));
        this.timerSpecificPingMessages = pingMessageSection.getBoolean("enable-timer-specific-messages") ? this.loadPingMessages(pingMessageSection.getStringList("timer-messages")) : null;
        this.maintenance = this.config.getBoolean("maintenance-enabled");
        this.commandsOnMaintenanceEnable = this.config.getStringList("commands-on-maintenance-enable");
        this.commandsOnMaintenanceDisable = this.config.getStringList("commands-on-maintenance-disable");
        this.customMaintenanceIcon = this.config.getBoolean("custom-maintenance-icon");
        this.joinNotifications = this.config.getBoolean("send-join-notification");
        this.broadcastIntervals = new HashSet<Integer>(this.config.getIntList("timer-broadcast-for-seconds"));
        if (this.plugin.getServerType() != ServerType.SPONGE) {
            ConfigSection playerCountMessageSection = this.config.getSection("player-count-message");
            this.customPlayerCountMessage = playerCountMessageSection.getBoolean("enabled");
            this.legacyParsedPlayerCountMessage = this.toLegacy(this.parse(this.getConfigMessage(playerCountMessageSection, "message")));
            this.legacyParsedTimerPlayerCountMessage = playerCountMessageSection.getBoolean("enable-timer-specific-message") ? this.toLegacy(this.parse(this.getConfigMessage(playerCountMessageSection, "timer-message"))) : null;
        }
        ConfigSection listHoverMessageSection = this.config.getSection("player-list-hover-message");
        this.customPlayerCountHoverMessage = listHoverMessageSection.getBoolean("enabled");
        this.legacyParsedPlayerCountHoverLines = new ArrayList<String>();
        for (String line : listHoverMessageSection.getString("message").split(NEW_LINE_REPLACEMENT)) {
            this.legacyParsedPlayerCountHoverLines.add(this.toLegacy(this.parse(line)));
        }
        if (listHoverMessageSection.getBoolean("enable-timer-specific-message")) {
            this.legacyParsedTimerPlayerCountHoverLines = new ArrayList<String>();
            for (String line : listHoverMessageSection.getString("timer-message").split(NEW_LINE_REPLACEMENT)) {
                this.legacyParsedTimerPlayerCountHoverLines.add(this.toLegacy(this.parse(line)));
            }
        } else {
            this.legacyParsedTimerPlayerCountHoverLines = null;
        }
        this.languageName = this.config.getString("language").toLowerCase();
        this.kickOnlinePlayers = this.config.getBoolean("kick-online-players", true);
        this.updateChecks = this.config.getBoolean("update-checks", true);
        this.debug = this.config.getBoolean("debug");
        ConfigSection section = this.config.getSection("continue-endtimer-after-restart");
        this.saveEndtimerOnStop = section.getBoolean("enabled");
        this.savedEndtimer = section.getLong("end");
        if (this.customMaintenanceIcon) {
            this.plugin.loadMaintenanceIcon();
        }
        this.whitelistedPlayers.clear();
        for (Map.Entry<String, Object> entry : this.whitelist.getValues().entrySet()) {
            try {
                this.whitelistedPlayers.put(UUID.fromString(entry.getKey()), String.valueOf(entry.getValue()));
            } catch (IllegalArgumentException e) {
                this.plugin.getLogger().warning("Invalid WhitelistedPlayers entry: " + entry.getKey());
            }
        }
        this.loadExtraSettings();
    }

    private void updatePluginDirectory() {
        String oldDirName = "Maintenance" + (Object)((Object)this.plugin.getServerType());
        if (this.plugin.getServerType() == ServerType.SPONGE || this.plugin.getServerType() == ServerType.VELOCITY) {
            oldDirName = oldDirName.toLowerCase(Locale.ROOT);
        } else if (this.plugin.getServerType() == ServerType.SPIGOT) {
            oldDirName = "MaintenanceSpigot";
        }
        File oldDir = new File(this.plugin.getDataFolder().getParentFile(), oldDirName);
        if (!oldDir.exists()) {
            return;
        }
        try {
            Files.move(oldDir.toPath(), this.plugin.getDataFolder().toPath(), new CopyOption[0]);
            this.plugin.getLogger().info("Moved old " + oldDirName + " to new " + this.plugin.getDataFolder().getName() + " directory!");
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Error while copying " + oldDirName + " to new " + this.plugin.getDataFolder().getName() + " directory!", e);
        }
    }

    private void updateConfig() {
        int version = this.config.getInt("config-version");
        if (version != 9) {
            this.plugin.getLogger().info("Updating config to the latest version...");
            if (version < 6) {
                this.config.getStringList("pingmessages").replaceAll(this::legacyToMinimessage);
                this.config.getStringList("timerspecific-pingmessages").replaceAll(this::legacyToMinimessage);
                this.config.set("playercounthovermessage", this.legacyToMinimessage(this.config.getString("playercounthovermessage")));
                this.config.set("playercountmessage", this.legacyToMinimessage(this.config.getString("playercountmessage")));
            }
            if (version < 9) {
                this.config.move("enable-pingmessages", "ping-message.enabled");
                this.config.move("pingmessages", "ping-message.messages");
                this.config.move("enable-timerspecific-messages", "ping-message.enable-timer-specific-messages");
                this.config.move("timerspecific-pingmessages", "ping-message.timer-messages");
                this.config.move("enable-playercountmessage", "player-count-message.enabled");
                this.config.move("playercountmessage", "player-count-message.message");
                this.config.move("enable-playercounthovermessage", "player-list-hover-message.enabled");
                this.config.move("playercounthovermessage", "player-list-hover-message.message");
            }
            this.createFile("config-new.yml", "config.yml");
            File file = new File(this.plugin.getDataFolder(), "config-new.yml");
            Config tempConfig = new Config(file, this.unsupportedFields);
            try {
                tempConfig.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.config.addMissingFields(tempConfig);
            this.config.replaceComments(tempConfig);
            this.config.set("config-version", 9);
            file.delete();
            tempConfig.clear();
            this.saveConfig();
            this.plugin.getLogger().info("Done! Updated config!");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void updateLanguageFile() throws IOException {
        int version = this.language.getInt("language-version");
        if (version == 2) {
            return;
        }
        this.plugin.getLogger().info("Updating language file to the latest version...");
        if (version < 1) {
            for (Map.Entry<String, Object> entry : this.language.getValues().entrySet()) {
                if (!(entry.getValue() instanceof String)) continue;
                String value = (String)entry.getValue();
                value = value.replace("&8[&eMaintenance&8] ", "<prefix>");
                value = this.legacyToMinimessage(value);
                value = value.replace("%NEWLINE%", NEW_LINE_REPLACEMENT);
                this.language.set(entry.getKey(), value);
            }
        }
        String filePrefix = "language-" + this.languageName;
        this.createFile(filePrefix + "-new.yml", filePrefix + ".yml");
        File file = new File(this.plugin.getDataFolder(), filePrefix + "-new.yml");
        Config tempConfig = new Config(file, new String[0]);
        try {
            tempConfig.load();
        } finally {
            file.delete();
        }
        this.language.addMissingFields(tempConfig);
        this.language.replaceComments(tempConfig);
        tempConfig.clear();
        this.language.set("language-version", 2);
        this.language.save();
        this.plugin.getLogger().info("Updated language file!");
    }

    private String legacyToMinimessage(String s) {
        TextComponent component = LegacyComponentSerializer.legacyAmpersand().deserialize(s);
        String serialized = (String)MiniMessage.miniMessage().serialize(component);
        return serialized.replaceAll("</[a-z_]+>", "");
    }

    private String getConfigMessage(ConfigSection section, String path) {
        String s = section.getString(path);
        if (s == null) {
            this.plugin.getLogger().warning("The config file is missing the following string: " + path);
            return path;
        }
        return this.replaceNewlineVar(s);
    }

    public String getConfigMessage(String path) {
        return this.getConfigMessage(this.config, path);
    }

    public String getLanguageString(String path, String ... replacements) {
        String s = this.language.getString(path);
        if (s == null) {
            this.plugin.getLogger().warning("The language file is missing the following string: " + path);
            return path;
        }
        if (replacements.length != 0) {
            if (replacements.length % 2 != 0) {
                throw new IllegalArgumentException("Invalid replacement count: " + replacements.length);
            }
            for (int i = 0; i < replacements.length; i += 2) {
                String key = replacements[i];
                s = s.replace(key, replacements[i + 1]);
            }
        }
        return this.replaceNewlineVar(s).replace("<prefix>", this.prefixString);
    }

    @Nullable
    public String getLanguageStringOrNull(String path, String ... replacements) {
        return this.language.contains(path) ? this.getLanguageString(path, replacements) : null;
    }

    public Component getMessage(String path, String ... replacements) {
        return this.parse(this.getLanguageString(path, replacements));
    }

    public Component getRandomPingMessage() {
        if (this.plugin.isTaskRunning() && !this.plugin.getRunnable().shouldEnable() && this.hasTimerSpecificPingMessages() && !this.timerSpecificPingMessages.isEmpty()) {
            return this.getPingMessage(this.timerSpecificPingMessages);
        }
        return this.pingMessages.isEmpty() ? Component.empty() : this.getPingMessage(this.pingMessages);
    }

    private Component getPingMessage(List<String> list) {
        String component = list.size() == 1 ? list.get(0) : list.get(RANDOM.nextInt(list.size()));
        return this.parse(this.plugin.replacePingVariables(component));
    }

    private List<String> loadPingMessages(List<String> list) {
        ArrayList<String> components = new ArrayList<String>(list.size());
        for (String s : list) {
            components.add(this.replaceNewlineVar(s));
        }
        return components;
    }

    @Override
    public boolean removeWhitelistedPlayer(UUID uuid) {
        if (this.whitelistedPlayers.remove(uuid) == null) {
            return false;
        }
        this.whitelist.remove(uuid.toString());
        this.saveWhitelistedPlayers();
        return true;
    }

    @Override
    public boolean removeWhitelistedPlayer(String name) {
        UUID uuid = null;
        for (Map.Entry<UUID, String> e : this.whitelistedPlayers.entrySet()) {
            if (!e.getValue().equalsIgnoreCase(name)) continue;
            uuid = e.getKey();
            break;
        }
        if (uuid == null) {
            return false;
        }
        this.whitelistedPlayers.remove(uuid);
        this.whitelist.remove(uuid.toString());
        this.saveWhitelistedPlayers();
        return true;
    }

    @Override
    public boolean addWhitelistedPlayer(UUID uuid, String name) {
        boolean added = this.whitelistedPlayers.put(uuid, name) == null;
        this.whitelist.set(uuid.toString(), name);
        this.saveWhitelistedPlayers();
        return added;
    }

    @Override
    public Map<UUID, String> getWhitelistedPlayers() {
        return this.whitelistedPlayers;
    }

    @Override
    public boolean isWhitelisted(UUID uuid) {
        return this.whitelistedPlayers.containsKey(uuid);
    }

    @Override
    public boolean isMaintenance() {
        return this.maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    @Override
    public boolean isEnablePingMessages() {
        return this.enablePingMessages;
    }

    @Override
    public boolean isJoinNotifications() {
        return this.joinNotifications;
    }

    @Override
    public boolean hasCustomIcon() {
        return this.customMaintenanceIcon;
    }

    @Override
    public boolean debugEnabled() {
        return this.debug;
    }

    public boolean hasUpdateChecks() {
        return this.updateChecks;
    }

    public boolean isSaveEndtimerOnStop() {
        return this.saveEndtimerOnStop;
    }

    public boolean hasTimerSpecificPingMessages() {
        return this.timerSpecificPingMessages != null;
    }

    public boolean isKickOnlinePlayers() {
        return this.kickOnlinePlayers;
    }

    public long getSavedEndtimer() {
        return this.savedEndtimer;
    }

    public void setSavedEndtimer(long millis) {
        if (this.savedEndtimer == millis) {
            return;
        }
        this.savedEndtimer = millis;
        this.config.getSection("continue-endtimer-after-restart").set("end", millis);
        this.saveConfig();
    }

    public Config getConfig() {
        return this.config;
    }

    public List<String> getPingMessages() {
        return this.pingMessages;
    }

    public List<String> getCommandsOnMaintenanceEnable() {
        return this.commandsOnMaintenanceEnable;
    }

    public List<String> getCommandsOnMaintenanceDisable() {
        return this.commandsOnMaintenanceDisable;
    }

    @Nullable
    public List<String> getTimerSpecificPingMessages() {
        return this.timerSpecificPingMessages;
    }

    public Set<Integer> getBroadcastIntervals() {
        return this.broadcastIntervals;
    }

    public String getLegacyParsedPlayerCountMessage() {
        String message = this.timerDependent(this.legacyParsedPlayerCountMessage, this.legacyParsedTimerPlayerCountMessage);
        return this.plugin.replacePingVariables(message);
    }

    public String[] getLegacyParsedPlayerCountHoverLines() {
        List<String> lines = this.timerDependent(this.legacyParsedPlayerCountHoverLines, this.legacyParsedTimerPlayerCountHoverLines);
        String[] parsedLines = new String[lines.size()];
        for (int i = 0; i < lines.size(); ++i) {
            parsedLines[i] = this.plugin.replacePingVariables(lines.get(i));
        }
        return parsedLines;
    }

    private <T> T timerDependent(T normal, @Nullable T timerSpecific) {
        return timerSpecific != null && this.plugin.isTaskRunning() && !this.plugin.getRunnable().shouldEnable() ? timerSpecific : normal;
    }

    public Component getKickMessage() {
        return this.parse(this.plugin.replacePingVariables(this.getLanguageString("kickmessage", new String[0])));
    }

    public String getLanguage() {
        return this.languageName;
    }

    public boolean hasCustomPlayerCountMessage() {
        return this.customPlayerCountMessage;
    }

    public boolean hasCustomPlayerCountHoverMessage() {
        return this.customPlayerCountHoverMessage;
    }

    public String getWebhookUrl() {
        return this.config.getWebhookUrl();
    }

    protected Component parse(String s) {
        return MiniMessage.miniMessage().deserialize(s);
    }

    protected String toLegacy(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    protected String replaceNewlineVar(String s) {
        return s.replace(NEW_LINE_REPLACEMENT, "\n");
    }

    protected void loadExtraSettings() {
    }
}

