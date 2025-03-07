/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.time.Duration;

public final class ScheduleTimerCommand
extends CommandInfo {
    public ScheduleTimerCommand(MaintenancePlugin plugin) {
        super(plugin, "timer");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (this.checkArgs(sender, args, 3)) {
            return;
        }
        Duration enableIn = this.plugin.getCommandManager().parseDurationAndCheckTask(sender, args[1]);
        Duration maintenanceDuration = this.plugin.getCommandManager().parseDurationAndCheckTask(sender, args[2], false);
        if (enableIn == null || maintenanceDuration == null) {
            sender.send(this.getHelpMessage());
            return;
        }
        if (this.plugin.isMaintenance()) {
            sender.send(this.getMessage("alreadyEnabled", new String[0]));
            return;
        }
        this.plugin.scheduleMaintenanceRunnable(enableIn, maintenanceDuration);
        sender.send(this.getMessage("scheduletimerStarted", "%TIME%", this.plugin.getRunnable().getTime(), "%DURATION%", this.plugin.getFormattedTime((int)maintenanceDuration.getSeconds())));
    }
}

