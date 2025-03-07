/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.proxy;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.config.ConfigSection;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.mysql.MySQL;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public final class SettingsProxy
extends Settings {
    private final MaintenanceProxyPlugin proxyPlugin;
    private Set<String> maintenanceServers;
    private List<String> fallbackServers;
    private String waitingServer;
    private boolean fallbackToOfflineUUID;
    private Map<String, List<String>> commandsOnMaintenanceEnable;
    private Map<String, List<String>> commandsOnMaintenanceDisable;
    private String mySQLTable;
    private String serverTable;
    private String maintenanceQuery;
    private String serverQuery;
    private MySQL mySQL;
    private long millisecondsToCheck;
    private long lastMySQLCheck;
    private long lastServerCheck;

    public SettingsProxy(MaintenanceProxyPlugin plugin) {
        super(plugin, new String[0]);
        this.proxyPlugin = plugin;
    }

    private void setupMySQL() {
        this.plugin.getLogger().info("Trying to open database connection... (also, you can simply ignore the SLF4J soft-warning if it shows up)");
        ConfigSection section = this.config.getSection("mysql");
        if (section == null) {
            this.plugin.getLogger().warning("Section missing: mysql");
            return;
        }
        this.mySQL = new MySQL(this.plugin.getLogger(), section.getString("host"), section.getInt("port"), section.getString("username"), section.getString("password"), section.getString("database"), section.getBoolean("use-ssl", true));
        this.mySQLTable = section.getString("table", "maintenance_settings");
        this.serverTable = section.getString("servertable", "maintenance_servers");
        this.mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.mySQLTable + " (setting VARCHAR(16) PRIMARY KEY, value VARCHAR(255))", new Object[0]);
        this.mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.serverTable + " (server VARCHAR(64) PRIMARY KEY)", new Object[0]);
        this.maintenanceQuery = "SELECT * FROM " + this.mySQLTable + " WHERE setting = ?";
        this.serverQuery = "SELECT * FROM " + this.serverTable;
        this.plugin.getLogger().info("Done!");
    }

    @Override
    protected void loadExtraSettings() {
        Object fallback;
        if (!this.hasMySQL() && this.config.getBoolean("mysql.use-mysql")) {
            try {
                this.setupMySQL();
            } catch (Exception e) {
                this.mySQL = null;
                this.plugin.getLogger().warning("Error while trying do open database connection!");
                e.printStackTrace();
            }
        }
        this.fallbackServers = (fallback = this.config.getObject("fallback")) instanceof String ? Collections.singletonList((String)fallback) : this.config.getStringList("fallback", Collections.emptyList());
        this.waitingServer = this.config.getString("waiting-server", "");
        if (this.waitingServer.isEmpty() || this.waitingServer.equalsIgnoreCase("none")) {
            this.waitingServer = null;
        }
        this.fallbackToOfflineUUID = this.config.getBoolean("fallback-to-offline-uuid", false);
        this.commandsOnMaintenanceEnable = new HashMap<String, List<String>>();
        ConfigSection enableCommandsSection = this.config.getSection("commands-on-single-maintenance-enable");
        for (String string : enableCommandsSection.getKeys()) {
            this.commandsOnMaintenanceEnable.put(string.toLowerCase(Locale.ROOT), enableCommandsSection.getStringList(string));
        }
        this.commandsOnMaintenanceDisable = new HashMap<String, List<String>>();
        ConfigSection disableCommandsSection = this.config.getSection("commands-on-single-maintenance-disable");
        for (String key : disableCommandsSection.getKeys()) {
            this.commandsOnMaintenanceDisable.put(key.toLowerCase(Locale.ROOT), disableCommandsSection.getStringList(key));
        }
        if (this.hasMySQL()) {
            this.maintenance = this.loadMaintenance();
            this.maintenanceServers = this.loadMaintenanceServersFromSQL();
            long l = this.config.getInt("mysql.update-interval");
            this.millisecondsToCheck = l > 0L ? l * 1000L : 500L;
            this.lastMySQLCheck = System.currentTimeMillis();
            this.lastServerCheck = System.currentTimeMillis();
        } else {
            List<String> list = this.config.getStringList("proxied-maintenance-servers");
            this.maintenanceServers = list == null ? new HashSet<String>() : new HashSet<String>(list);
        }
    }

    @Override
    public boolean isMaintenance() {
        if (this.hasMySQL() && System.currentTimeMillis() - this.lastMySQLCheck > this.millisecondsToCheck) {
            boolean databaseValue = this.loadMaintenance();
            if (databaseValue != this.maintenance) {
                this.maintenance = databaseValue;
                this.plugin.serverActions(this.maintenance);
            }
            this.lastMySQLCheck = System.currentTimeMillis();
        }
        return this.maintenance;
    }

    public boolean isMaintenance(String serverName) {
        if (this.hasMySQL() && System.currentTimeMillis() - this.lastServerCheck > this.millisecondsToCheck) {
            Set<String> databaseValue = this.loadMaintenanceServersFromSQL();
            if (!this.maintenanceServers.equals(databaseValue)) {
                for (String s : databaseValue) {
                    if (this.maintenanceServers.contains(s)) continue;
                    this.proxyPlugin.serverActions(this.proxyPlugin.getServer(s), true);
                }
                for (String s : this.maintenanceServers) {
                    if (databaseValue.contains(s)) continue;
                    this.proxyPlugin.serverActions(this.proxyPlugin.getServer(s), false);
                }
                this.maintenanceServers = databaseValue;
            }
            this.lastServerCheck = System.currentTimeMillis();
        }
        return this.maintenanceServers.contains(serverName);
    }

    public Component getServerKickMessage(String server) {
        String message = this.getLanguageStringOrNull("singleMaintenanceKicks." + server, "%SERVER%", server);
        if (message == null) {
            message = this.getLanguageString("singleMaintenanceKick", "%SERVER%", server);
        }
        return this.parse(this.plugin.replacePingVariables(message));
    }

    public Component getFullServerKickMessage(String server) {
        String message = this.getLanguageStringOrNull("singleMaintenanceKicksComplete." + server, "%SERVER%", server);
        if (message == null) {
            message = this.getLanguageString("singleMaintenanceKickComplete", "%SERVER%", server);
        }
        return this.parse(this.plugin.replacePingVariables(message));
    }

    public boolean hasMySQL() {
        return this.mySQL != null;
    }

    void setMaintenanceToSQL(boolean maintenance) {
        this.plugin.async(() -> {
            String s = String.valueOf(maintenance);
            this.mySQL.executeUpdate("INSERT INTO " + this.mySQLTable + " (setting, value) VALUES (?, ?) ON DUPLICATE KEY UPDATE value = ?", "maintenance", s, s);
            this.lastMySQLCheck = System.currentTimeMillis();
        });
    }

    boolean addMaintenanceServer(String server) {
        if (this.hasMySQL()) {
            this.maintenanceServers = this.loadMaintenanceServersFromSQL();
            if (!this.maintenanceServers.add(server)) {
                return false;
            }
            this.plugin.async(() -> this.mySQL.executeUpdate("INSERT INTO " + this.serverTable + " (server) VALUES (?)", server));
            this.lastServerCheck = System.currentTimeMillis();
        } else {
            if (!this.maintenanceServers.add(server)) {
                return false;
            }
            this.saveServersToConfig();
        }
        return true;
    }

    boolean removeMaintenanceServer(String server) {
        if (this.hasMySQL()) {
            this.maintenanceServers = this.loadMaintenanceServersFromSQL();
            if (!this.maintenanceServers.remove(server)) {
                return false;
            }
            this.plugin.async(() -> this.mySQL.executeUpdate("DELETE FROM " + this.serverTable + " WHERE server = ?", server));
            this.lastServerCheck = System.currentTimeMillis();
        } else {
            if (!this.maintenanceServers.remove(server)) {
                return false;
            }
            this.saveServersToConfig();
        }
        return true;
    }

    private Set<String> loadMaintenanceServersFromSQL() {
        HashSet<String> maintenanceServers = new HashSet<String>();
        this.mySQL.executeQuery(this.serverQuery, rs -> {
            try {
                while (rs.next()) {
                    maintenanceServers.add(rs.getString("server"));
                }
            } catch (SQLException e) {
                this.plugin.getLogger().warning("An error occured while trying to get the list of single servers with maintenance!");
                e.printStackTrace();
            }
        }, new Object[0]);
        return maintenanceServers;
    }

    private boolean loadMaintenance() {
        boolean[] databaseValue = new boolean[]{false};
        this.mySQL.executeQuery(this.maintenanceQuery, rs -> {
            try {
                if (rs.next()) {
                    databaseValue[0] = Boolean.parseBoolean(rs.getString("value"));
                }
            } catch (SQLException e) {
                this.plugin.getLogger().warning("An error occured while trying to get the maintenance value from the database!");
                e.printStackTrace();
            }
        }, "maintenance");
        return databaseValue[0];
    }

    private void saveServersToConfig() {
        this.config.set("proxied-maintenance-servers", new ArrayList<String>(this.maintenanceServers));
        this.saveConfig();
    }

    public Set<String> getMaintenanceServers() {
        return this.maintenanceServers;
    }

    @Nullable
    public Server getFallbackServer() {
        for (String fallbackServer : this.fallbackServers) {
            Server server = this.proxyPlugin.getServer(fallbackServer);
            if (server == null || this.isMaintenance(server.getName())) continue;
            return server;
        }
        return null;
    }

    @Nullable
    public String getWaitingServer() {
        return this.waitingServer;
    }

    public boolean isFallbackToOfflineUUID() {
        return this.fallbackToOfflineUUID;
    }

    public List<String> getCommandsOnMaintenanceEnable(Server server) {
        List enableCommands = this.commandsOnMaintenanceEnable.getOrDefault("all", new ArrayList());
        List<String> serverEnableCommands = this.commandsOnMaintenanceEnable.get(server.getName().toLowerCase(Locale.ROOT));
        if (serverEnableCommands != null) {
            enableCommands.addAll(serverEnableCommands);
        }
        return enableCommands;
    }

    public List<String> getCommandsOnMaintenanceDisable(Server server) {
        List disableCommands = this.commandsOnMaintenanceDisable.getOrDefault("all", new ArrayList());
        List<String> serverDisableCommands = this.commandsOnMaintenanceDisable.get(server.getName().toLowerCase(Locale.ROOT));
        if (serverDisableCommands != null) {
            disableCommands.addAll(serverDisableCommands);
        }
        return disableCommands;
    }

    @Nullable
    MySQL getMySQL() {
        return this.mySQL;
    }
}

