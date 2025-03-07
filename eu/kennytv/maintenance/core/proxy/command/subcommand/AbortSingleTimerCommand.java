/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.command.subcommand;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.command.ProxyCommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.util.Collections;
import java.util.List;

public final class AbortSingleTimerCommand
extends ProxyCommandInfo {
    public AbortSingleTimerCommand(MaintenanceProxyPlugin plugin) {
        super(plugin, null);
    }

    @Override
    public boolean hasPermission(SenderInfo sender) {
        return sender.hasMaintenancePermission("timer") || sender.hasPermission("maintenance.singleserver.timer");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (args.length == 1) {
            if (this.checkPermission(sender, "timer")) {
                return;
            }
            if (!this.plugin.isTaskRunning()) {
                sender.send(this.getMessage("timerNotRunning", new String[0]));
                return;
            }
            this.plugin.cancelTask();
            sender.send(this.getMessage("timerCancelled", new String[0]));
        } else if (args.length == 2) {
            if (this.checkPermission(sender, "singleserver.timer")) {
                return;
            }
            Server server = this.plugin.getServer(args[1]);
            if (server == null) {
                sender.send(this.getMessage("serverNotFound", new String[0]));
                return;
            }
            if (!this.plugin.isServerTaskRunning(server)) {
                sender.send(this.getMessage("singleTimerNotRunning", new String[0]));
                return;
            }
            this.plugin.cancelSingleTask(server);
            sender.send(this.getMessage("singleTimerCancelled", "%SERVER%", server.getName()));
        } else {
            sender.send(this.getHelpMessage());
        }
    }

    @Override
    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        return args.length == 2 && sender.hasMaintenancePermission("singleserver.timer") ? this.plugin.getCommandManager().getServersCompletion(args[1]) : Collections.emptyList();
    }
}

