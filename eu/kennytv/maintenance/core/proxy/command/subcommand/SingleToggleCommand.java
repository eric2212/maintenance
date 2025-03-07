/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.command.subcommand;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.command.ProxyCommandInfo;
import eu.kennytv.maintenance.core.proxy.server.DummyServer;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.util.Collections;
import java.util.List;

public final class SingleToggleCommand
extends ProxyCommandInfo {
    public SingleToggleCommand(MaintenanceProxyPlugin plugin) {
        super(plugin, null);
    }

    @Override
    public boolean hasPermission(SenderInfo sender) {
        return sender.hasMaintenancePermission("toggle") || sender.hasPermission("maintenance.singleserver.toggle");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (args.length == 1) {
            if (this.checkPermission(sender, "toggle")) {
                return;
            }
            boolean maintenance = args[0].equalsIgnoreCase("on");
            if (maintenance == this.plugin.isMaintenance()) {
                sender.send(this.getMessage(maintenance ? "alreadyEnabled" : "alreadyDisabled", new String[0]));
                return;
            }
            this.plugin.setMaintenance(maintenance);
        } else if (args.length == 2) {
            if (this.checkPermission(sender, "singleserver.toggle")) {
                return;
            }
            boolean maintenance = args[0].equalsIgnoreCase("on");
            Server server = this.plugin.getServer(args[1]);
            if (server == null) {
                if (maintenance) {
                    sender.send(this.getMessage("serverNotFound", new String[0]));
                    return;
                }
                server = new DummyServer(args[1]);
                if (!this.plugin.isMaintenance(server)) {
                    sender.send(this.getMessage("serverNotFound", new String[0]));
                    return;
                }
            }
            if (this.plugin.setMaintenanceToServer(server, maintenance)) {
                if (!sender.isPlayer() || !server.getName().equals(this.plugin.getServerNameOf(sender))) {
                    sender.send(this.getMessage(maintenance ? "singleMaintenanceActivated" : "singleMaintenanceDeactivated", "%SERVER%", server.getName()));
                }
            } else {
                sender.send(this.getMessage(maintenance ? "singleServerAlreadyEnabled" : "singleServerAlreadyDisabled", "%SERVER%", server.getName()));
            }
        } else {
            sender.send(this.getHelpMessage());
        }
    }

    @Override
    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        if (args.length != 2 || !sender.hasMaintenancePermission("singleserver.toggle")) {
            return Collections.emptyList();
        }
        return args[0].equalsIgnoreCase("off") ? this.plugin.getCommandManager().getMaintenanceServersCompletion(args[1].toLowerCase()) : this.plugin.getCommandManager().getServersCompletion(args[1].toLowerCase());
    }
}

