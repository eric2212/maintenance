/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.command.subcommand;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.command.ProxyCommandInfo;
import eu.kennytv.maintenance.core.runnable.MaintenanceRunnableBase;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public final class SingleScheduleTimerCommand
extends ProxyCommandInfo {
    public SingleScheduleTimerCommand(MaintenanceProxyPlugin plugin) {
        super(plugin, null);
    }

    @Override
    public boolean hasPermission(SenderInfo sender) {
        return sender.hasMaintenancePermission("timer") || sender.hasPermission("maintenance.singleserver.timer");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (args.length == 3) {
            if (this.checkPermission(sender, "timer")) {
                return;
            }
            Duration enableIn = this.plugin.getCommandManager().parseDurationAndCheckTask(sender, args[1]);
            Duration duration = this.plugin.getCommandManager().parseDurationAndCheckTask(sender, args[2], false);
            if (enableIn == null || duration == null) {
                sender.send(this.getHelpMessage());
                return;
            }
            if (this.plugin.isMaintenance()) {
                sender.send(this.getMessage("alreadyEnabled", new String[0]));
                return;
            }
            this.plugin.scheduleMaintenanceRunnable(duration, enableIn);
            sender.send(this.getMessage("scheduletimerStarted", "%TIME%", this.plugin.getRunnable().getTime(), "%DURATION%", this.plugin.getFormattedTime((int)duration.getSeconds())));
        } else if (args.length == 4) {
            if (this.checkPermission(sender, "singleserver.timer")) {
                return;
            }
            Duration enableIn = this.plugin.getCommandManager().parseDurationAndCheckTask(sender, args[2], false);
            Duration duration = this.plugin.getCommandManager().parseDurationAndCheckTask(sender, args[3], false);
            if (enableIn == null || duration == null) {
                sender.send(this.getHelpMessage());
                return;
            }
            Server server = this.plugin.getCommandManager().checkSingleTimerServerArg(sender, args[1]);
            if (server == null) {
                return;
            }
            if (this.plugin.isMaintenance(server)) {
                sender.send(this.getMessage("singleServerAlreadyEnabled", "%SERVER%", server.getName()));
                return;
            }
            MaintenanceRunnableBase runnable = this.plugin.scheduleSingleMaintenanceRunnable(server, enableIn, duration);
            sender.send(this.getMessage("singleScheduletimerStarted", "%TIME%", runnable.getTime(), "%DURATION%", this.plugin.getFormattedTime((int)duration.getSeconds()), "%SERVER%", server.getName()));
        } else {
            sender.send(this.getHelpMessage());
        }
    }

    @Override
    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        return args.length == 2 && sender.hasMaintenancePermission("singleserver.timer") ? this.plugin.getCommandManager().getServersCompletion(args[1].toLowerCase()) : Collections.emptyList();
    }
}

