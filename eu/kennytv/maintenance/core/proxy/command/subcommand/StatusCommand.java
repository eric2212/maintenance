/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.command.subcommand;

import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.command.ProxyCommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;

public final class StatusCommand
extends ProxyCommandInfo {
    public StatusCommand(MaintenanceProxyPlugin plugin) {
        super(plugin, "singleserver.status");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (this.getSettings().getMaintenanceServers().isEmpty()) {
            sender.send(this.getMessage("singleServerMaintenanceListEmpty", new String[0]));
            return;
        }
        sender.send(this.getMessage("singleServerMaintenanceList", new String[0]));
        for (String server : this.getSettings().getMaintenanceServers()) {
            sender.send(this.getMessage("singleServerMaintenanceListEntry", "%SERVER%", server));
        }
    }
}

