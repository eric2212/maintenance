/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.util.Collections;
import java.util.List;

public final class MotdCommand
extends CommandInfo {
    public MotdCommand(MaintenancePlugin plugin) {
        super(plugin, "motd");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (args.length == 1) {
            this.sendList(sender, this.getSettings().getPingMessages());
        } else if (args.length == 2 && args[1].equalsIgnoreCase("timer")) {
            if (!this.getSettings().hasTimerSpecificPingMessages()) {
                sender.send(this.getMessage("timerMotdDisabled", new String[0]));
                return;
            }
            this.sendList(sender, this.getSettings().getTimerSpecificPingMessages());
        } else {
            sender.send(this.getHelpMessage());
        }
    }

    private void sendList(SenderInfo sender, List<String> list) {
        if (list == null || list.isEmpty()) {
            sender.send(this.getMessage("motdListEmpty", new String[0]));
            return;
        }
        sender.send(this.getMessage("motdList", new String[0]));
        for (int i = 0; i < list.size(); ++i) {
            sender.sendRich("<aqua>" + (i + 1) + "<dark_gray><st>---------");
            sender.sendRich(list.get(i));
        }
        sender.sendRich("<dark_gray><st>----------");
    }

    @Override
    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        if (args.length != 2 || !this.getSettings().hasTimerSpecificPingMessages()) {
            return Collections.emptyList();
        }
        return Collections.singletonList("timer");
    }
}

