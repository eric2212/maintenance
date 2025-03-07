/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package eu.kennytv.maintenance.core.dump;

import com.google.gson.JsonObject;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.dump.ServerDump;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MaintenanceDump {
    private final ServerDump general;
    private final Map<String, Object> configuration;
    private final JsonObject plugins;

    public MaintenanceDump(MaintenancePlugin plugin, Settings settings) {
        this.general = new ServerDump(plugin.getVersion(), plugin.getServerType().toString(), plugin.getServerVersion(), plugin.getMaintenanceServersDump());
        this.configuration = new LinkedHashMap<String, Object>(settings.getConfig().getValues());
        Object o = this.configuration.get("mysql");
        if (o instanceof Map) {
            LinkedHashMap map = new LinkedHashMap((Map)o);
            map.keySet().removeIf(key -> !key.equals("use-mysql") && !key.equals("update-interval"));
            this.configuration.put("mysql", map);
        }
        this.configuration.put("whitelisted-players", settings.getWhitelistedPlayers());
        this.configuration.put("icon-exists", new File(plugin.getDataFolder(), "maintenance-icon.png").exists());
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("plugins", MaintenancePlugin.GSON.toJsonTree(plugin.getPlugins()));
        this.plugins = jsonObject;
    }
}

