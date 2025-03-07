/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.config.ConfigSection;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RemoveMotdCommand
extends CommandInfo {
    public RemoveMotdCommand(MaintenancePlugin plugin) {
        super(plugin, "setmotd");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        boolean timerPingMessages = false;
        if (args.length == 3 && args[1].equalsIgnoreCase("timer")) {
            if (!this.getSettings().hasTimerSpecificPingMessages()) {
                sender.send(this.getMessage("timerMotdDisabled", new String[0]));
                return;
            }
            args = this.plugin.removeArrayIndex(args, 1);
            timerPingMessages = true;
        }
        if (this.checkArgs(sender, args, 2)) {
            return;
        }
        if (!this.plugin.isNumeric(args[1])) {
            sender.send(this.getHelpMessage());
            return;
        }
        Settings settings = this.getSettings();
        ConfigSection section = settings.getConfig().getSection("ping-message");
        List<String> pingMessages = section.getStringList(timerPingMessages ? "timer-messages" : "messages");
        if (pingMessages.size() < 2) {
            sender.send(this.getMessage("removeMotdError", new String[0]));
            return;
        }
        int index = Integer.parseInt(args[1]);
        if (index == 0 || index > pingMessages.size()) {
            sender.send(this.getMessage("setMotdIndexError", "%MOTDS%", Integer.toString(pingMessages.size()), "%NEWAMOUNT%", Integer.toString(pingMessages.size())));
            return;
        }
        List<String> pingComponents = timerPingMessages ? settings.getTimerSpecificPingMessages() : settings.getPingMessages();
        pingComponents.remove(index - 1);
        pingMessages.remove(index - 1);
        section.set(timerPingMessages ? "timer-messages" : "messages", pingMessages);
        settings.saveConfig();
        sender.send(this.getMessage("removedMotd", "%INDEX%", args[1]));
    }

    @Override
    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        if (args.length == 2 || args.length == 3 && args[1].equalsIgnoreCase("timer")) {
            int size = (args.length == 3 ? this.plugin.getSettings().getTimerSpecificPingMessages() : this.plugin.getSettings().getPingMessages()).size();
            ArrayList<String> list = new ArrayList<String>(size);
            for (int i = 1; i <= size; ++i) {
                list.add(Integer.toString(i));
            }
            return list;
        }
        return Collections.emptyList();
    }
}

