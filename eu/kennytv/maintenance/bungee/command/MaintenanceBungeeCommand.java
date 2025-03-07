/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.config.ServerInfo
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 */
package eu.kennytv.maintenance.bungee.command;

import eu.kennytv.maintenance.bungee.MaintenanceBungeePlugin;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;
import eu.kennytv.maintenance.core.proxy.command.MaintenanceProxyCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class MaintenanceBungeeCommand
extends MaintenanceProxyCommand {
    private final MaintenanceBungeePlugin plugin;

    public MaintenanceBungeeCommand(MaintenanceBungeePlugin plugin, SettingsProxy settings) {
        super(plugin, settings);
        this.plugin = plugin;
        this.registerCommands();
    }

    @Override
    public List<String> getServersCompletion(String s) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry entry : this.plugin.getProxy().getServers().entrySet()) {
            String serverName = ((ServerInfo)entry.getValue()).getName();
            if (!((String)entry.getKey()).toLowerCase().startsWith(s) || this.plugin.getSettingsProxy().getMaintenanceServers().contains(serverName)) continue;
            list.add(serverName);
        }
        return list;
    }

    @Override
    public List<String> getPlayersCompletion() {
        ArrayList<String> list = new ArrayList<String>();
        for (ProxiedPlayer p : this.plugin.getProxy().getPlayers()) {
            list.add(p.getName());
        }
        return list;
    }
}

