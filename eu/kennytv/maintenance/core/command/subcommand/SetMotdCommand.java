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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SetMotdCommand
extends CommandInfo {
    public SetMotdCommand(MaintenancePlugin plugin) {
        super(plugin, "setmotd");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        String oldMessage;
        boolean timerPingMessages = false;
        if (args.length > 1 && args[1].equalsIgnoreCase("timer")) {
            if (!this.getSettings().hasTimerSpecificPingMessages()) {
                sender.send(this.getMessage("timerMotdDisabled", new String[0]));
                return;
            }
            args = this.plugin.removeArrayIndex(args, 1);
            timerPingMessages = true;
        }
        if (args.length < 4 || !this.plugin.isNumeric(args[1])) {
            sender.send(this.getHelpMessage());
            return;
        }
        Settings settings = this.getSettings();
        List<String> pingComponents = timerPingMessages ? settings.getTimerSpecificPingMessages() : settings.getPingMessages();
        ConfigSection section = settings.getConfig().getSection("ping-message");
        List<String> pingMessages = section.getStringList(timerPingMessages ? "timer-messages" : "messages");
        int index = Integer.parseInt(args[1]);
        if (index == 0 || index > pingMessages.size() + 1) {
            sender.send(this.getMessage("setMotdIndexError", "%MOTDS%", Integer.toString(pingMessages.size()), "%NEWAMOUNT%", Integer.toString(pingMessages.size() + 1)));
            return;
        }
        if (!this.plugin.isNumeric(args[2])) {
            sender.send(this.getMessage("setMotdLineError", new String[0]));
            return;
        }
        int line = Integer.parseInt(args[2]);
        if (line != 1 && line != 2) {
            sender.send(this.getMessage("setMotdLineError", new String[0]));
            return;
        }
        String message = String.join((CharSequence)" ", Arrays.copyOfRange(args, 3, args.length));
        String string = oldMessage = index > pingMessages.size() ? "" : pingMessages.get(index - 1);
        String newMessage = line == 1 ? (oldMessage.contains("<br>") ? message + "<br>" + oldMessage.split("<br>", 2)[1] : message) : (oldMessage.contains("<br>") ? oldMessage.split("<br>", 2)[0] + "<br>" + message : oldMessage + "<br>" + message);
        String componentString = newMessage.replace("<br>", "\n");
        if (index > pingMessages.size()) {
            pingMessages.add(newMessage);
            pingComponents.add(componentString);
        } else {
            pingMessages.set(index - 1, newMessage);
            pingComponents.set(index - 1, componentString);
        }
        section.set(timerPingMessages ? "timer-messages" : "messages", pingMessages);
        settings.saveConfig();
        sender.send(settings.getMessage("setMotd", "%LINE%", args[2], "%INDEX%", args[1], "%MOTD%", componentString));
    }

    @Override
    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        boolean timer = false;
        if (args.length > 1 && args[1].equalsIgnoreCase("timer")) {
            args = this.plugin.removeArrayIndex(args, 1);
            timer = true;
        }
        if (args.length == 3) {
            return Arrays.asList("1", "2");
        }
        if (args.length == 2) {
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 1; i <= (timer ? this.getSettings().getTimerSpecificPingMessages() : this.getSettings().getPingMessages()).size() + 1; ++i) {
                list.add(String.valueOf(i));
            }
            return list;
        }
        return Collections.emptyList();
    }
}

