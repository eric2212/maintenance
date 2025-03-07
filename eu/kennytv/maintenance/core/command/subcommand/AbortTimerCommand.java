/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;

public final class AbortTimerCommand
extends CommandInfo {
    public AbortTimerCommand(MaintenancePlugin plugin) {
        super(plugin, "timer");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (args.length != 1) {
            sender.send(this.getHelpMessage());
            return;
        }
        if (!this.plugin.isTaskRunning()) {
            sender.send(this.getMessage("timerNotRunning", new String[0]));
            return;
        }
        this.plugin.cancelTask();
        sender.send(this.getMessage("timerCancelled", new String[0]));
    }
}

