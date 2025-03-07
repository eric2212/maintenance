/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.time.Duration;

public final class EndtimerCommand
extends CommandInfo {
    public EndtimerCommand(MaintenancePlugin plugin) {
        super(plugin, "timer");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (this.checkArgs(sender, args, 2)) {
            return;
        }
        Duration duration = this.plugin.getCommandManager().parseDurationAndCheckTask(sender, args[1]);
        if (duration == null) {
            sender.send(this.getHelpMessage());
            return;
        }
        if (!this.plugin.isMaintenance()) {
            sender.send(this.getMessage("alreadyDisabled", new String[0]));
            return;
        }
        this.plugin.startMaintenanceRunnable(duration, false);
        sender.send(this.getMessage("endtimerStarted", "%TIME%", this.plugin.getRunnable().getTime()));
    }
}

