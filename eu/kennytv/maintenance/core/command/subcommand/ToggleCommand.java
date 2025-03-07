/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;

public final class ToggleCommand
extends CommandInfo {
    public ToggleCommand(MaintenancePlugin plugin) {
        super(plugin, "toggle");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (this.checkArgs(sender, args, 1)) {
            return;
        }
        boolean maintenance = args[0].equalsIgnoreCase("on");
        if (maintenance == this.plugin.isMaintenance()) {
            sender.send(this.getMessage(maintenance ? "alreadyEnabled" : "alreadyDisabled", new String[0]));
            return;
        }
        this.plugin.setMaintenance(maintenance);
    }
}

