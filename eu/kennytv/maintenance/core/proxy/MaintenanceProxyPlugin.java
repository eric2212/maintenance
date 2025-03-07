/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.Blocking
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.proxy;

import com.google.common.io.CharStreams;
import com.google.gson.JsonObject;
import eu.kennytv.maintenance.api.event.proxy.ServerMaintenanceChangedEvent;
import eu.kennytv.maintenance.api.proxy.MaintenanceProxy;
import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;
import eu.kennytv.maintenance.core.proxy.command.MaintenanceProxyCommand;
import eu.kennytv.maintenance.core.proxy.runnable.SingleMaintenanceRunnable;
import eu.kennytv.maintenance.core.proxy.runnable.SingleMaintenanceScheduleRunnable;
import eu.kennytv.maintenance.core.proxy.util.ProfileLookup;
import eu.kennytv.maintenance.core.runnable.MaintenanceRunnableBase;
import eu.kennytv.maintenance.core.util.RateLimitedException;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.core.util.ServerType;
import eu.kennytv.maintenance.core.util.Task;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Nullable;

public abstract class MaintenanceProxyPlugin
extends MaintenancePlugin
implements MaintenanceProxy {
    private final Map<String, Task> serverTasks = new HashMap<String, Task>();
    protected SettingsProxy settingsProxy;

    protected MaintenanceProxyPlugin(String version, ServerType serverType) {
        super(version, serverType);
    }

    @Override
    public void disable() {
        super.disable();
        if (this.settingsProxy.getMySQL() != null) {
            this.settingsProxy.getMySQL().close();
        }
    }

    @Override
    public void setMaintenance(boolean maintenance) {
        if (this.settingsProxy.hasMySQL()) {
            this.settingsProxy.setMaintenanceToSQL(maintenance);
        }
        super.setMaintenance(maintenance);
    }

    @Override
    public boolean isMaintenance(Server server) {
        return this.settingsProxy.isMaintenance(server.getName());
    }

    @Override
    public boolean setMaintenanceToServer(Server server, boolean maintenance) {
        if (maintenance ? !this.settingsProxy.addMaintenanceServer(server.getName()) : !this.settingsProxy.removeMaintenanceServer(server.getName())) {
            return false;
        }
        this.serverActions(server, maintenance);
        for (String command : maintenance ? this.settingsProxy.getCommandsOnMaintenanceEnable(server) : this.settingsProxy.getCommandsOnMaintenanceDisable(server)) {
            try {
                this.executeConsoleCommand(command.replace("%SERVER%", server.getName()));
            } catch (Exception e) {
                this.getLogger().severe("Error while executing extra maintenance " + (maintenance ? "enable" : "disable") + " command: " + command);
                e.printStackTrace();
            }
        }
        return true;
    }

    public void serverActions(Server server, boolean maintenance) {
        if (server == null) {
            return;
        }
        if (server.isRegisteredServer()) {
            if (maintenance) {
                Server fallback = this.settingsProxy.getFallbackServer();
                if (fallback == null && server.hasPlayers()) {
                    this.getLogger().warning("The set fallback could not be found! Instead kicking players from that server off the network!");
                }
                this.kickPlayers(server, fallback);
            } else {
                server.broadcast(this.settingsProxy.getMessage("singleMaintenanceDeactivated", "%SERVER%", server.getName()));
            }
            this.cancelSingleTask(server);
        }
        this.eventManager.callEvent(new ServerMaintenanceChangedEvent(server, maintenance));
    }

    @Override
    public boolean isServerTaskRunning(Server server) {
        return this.serverTasks.containsKey(server.getName());
    }

    @Override
    public Set<String> getMaintenanceServers() {
        return Collections.unmodifiableSet(this.settingsProxy.getMaintenanceServers());
    }

    public void cancelSingleTask(Server server) {
        Task task = this.serverTasks.remove(server.getName());
        if (task != null) {
            task.cancel();
        }
    }

    public MaintenanceRunnableBase startSingleMaintenanceRunnable(Server server, Duration duration, boolean enable) {
        SingleMaintenanceRunnable runnable = new SingleMaintenanceRunnable(this, this.settingsProxy, (int)duration.getSeconds(), enable, server);
        this.serverTasks.put(server.getName(), runnable.getTask());
        return runnable;
    }

    public MaintenanceRunnableBase scheduleSingleMaintenanceRunnable(Server server, Duration enableIn, Duration maintenanceDuration) {
        SingleMaintenanceScheduleRunnable runnable = new SingleMaintenanceScheduleRunnable((MaintenancePlugin)this, (Settings)this.settingsProxy, (int)enableIn.getSeconds(), (int)maintenanceDuration.getSeconds(), server);
        this.serverTasks.put(server.getName(), runnable.getTask());
        return runnable;
    }

    @Override
    @Nullable
    public List<String> getMaintenanceServersDump() {
        ArrayList<String> list = new ArrayList<String>();
        if (this.isMaintenance()) {
            list.add("global");
        }
        list.addAll(this.settingsProxy.getMaintenanceServers());
        return list.isEmpty() ? null : list;
    }

    @Override
    public MaintenanceProxyCommand getCommandManager() {
        return (MaintenanceProxyCommand)this.commandManager;
    }

    @Override
    protected void kickPlayers() {
        Server waitingServer;
        if (this.settingsProxy.getWaitingServer() != null && (waitingServer = this.getServer(this.settingsProxy.getWaitingServer())) != null) {
            this.kickPlayersTo(waitingServer);
            return;
        }
        this.kickPlayersFromProxy();
    }

    @Blocking
    @Nullable
    protected ProfileLookup doUUIDLookup(String name) throws IOException {
        ProfileLookup profileLookup;
        try {
            profileLookup = this.doUUIDLookupMojangAPI(name);
        } catch (RateLimitedException e) {
            profileLookup = this.doUUIDLookupAshconAPI(name);
        }
        if (this.settingsProxy.isFallbackToOfflineUUID() && profileLookup == null) {
            return new ProfileLookup(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8)), name);
        }
        return profileLookup;
    }

    @Nullable
    private ProfileLookup doUUIDLookupMojangAPI(String name) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        int status = connection.getResponseCode();
        if (status == 429) {
            throw new RateLimitedException();
        }
        if (status == 404) {
            return null;
        }
        try (InputStream in = connection.getInputStream();){
            String output = CharStreams.toString(new InputStreamReader(in));
            JsonObject json = (JsonObject)GSON.fromJson(output, JsonObject.class);
            UUID uuid = this.fromStringUUIDWithoutDashes(json.getAsJsonPrimitive("id").getAsString());
            String username = json.getAsJsonPrimitive("name").getAsString();
            ProfileLookup profileLookup = new ProfileLookup(uuid, username);
            return profileLookup;
        }
    }

    @Nullable
    private ProfileLookup doUUIDLookupAshconAPI(String name) throws IOException {
        URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + name);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        if (connection.getResponseCode() == 403) {
            return null;
        }
        try (InputStream in = connection.getInputStream();){
            String output = CharStreams.toString(new InputStreamReader(in));
            JsonObject json = (JsonObject)GSON.fromJson(output, JsonObject.class);
            UUID uuid = UUID.fromString(json.getAsJsonPrimitive("uuid").getAsString());
            String username = json.getAsJsonPrimitive("username").getAsString();
            ProfileLookup profileLookup = new ProfileLookup(uuid, username);
            return profileLookup;
        }
    }

    private UUID fromStringUUIDWithoutDashes(String undashedUUID) {
        return UUID.fromString(undashedUUID.substring(0, 8) + "-" + undashedUUID.substring(8, 12) + "-" + undashedUUID.substring(12, 16) + "-" + undashedUUID.substring(16, 20) + "-" + undashedUUID.substring(20, 32));
    }

    public SettingsProxy getSettingsProxy() {
        return this.settingsProxy;
    }

    @Nullable
    public abstract String getServerNameOf(SenderInfo var1);

    protected abstract void kickPlayers(Server var1, Server var2);

    protected abstract void kickPlayersTo(Server var1);

    protected abstract void kickPlayersFromProxy();
}

