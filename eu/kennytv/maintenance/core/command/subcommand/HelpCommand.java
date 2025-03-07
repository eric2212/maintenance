/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import com.google.common.base.Preconditions;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.util.ArrayList;
import java.util.List;

public final class HelpCommand
extends CommandInfo {
    private static final int COMMANDS_PER_PAGE = 8;

    public HelpCommand(MaintenancePlugin plugin) {
        super(plugin, null);
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (args.length > 2) {
            sender.send(this.getHelpMessage());
            return;
        }
        if (args.length == 1) {
            this.sendUsage(sender);
            return;
        }
        if (!this.plugin.isNumeric(args[1])) {
            sender.send(this.getHelpMessage());
            return;
        }
        int page = Integer.parseInt(args[1]);
        this.sendUsage(sender, page == 0 ? 1 : page);
    }

    public void sendUsage(SenderInfo sender) {
        this.sendUsage(sender, 1);
    }

    public void sendUsage(SenderInfo sender, int page) {
        Preconditions.checkArgument(page > 0);
        ArrayList<Component> commands = new ArrayList<Component>();
        for (CommandInfo cmd : this.plugin.getCommandManager().getCommands()) {
            if (!cmd.isVisible() || !cmd.hasPermission(sender)) continue;
            commands.add(cmd.getHelpMessage());
        }
        if ((page - 1) * 8 >= commands.size()) {
            sender.send(this.getMessage("helpPageNotFound", new String[0]));
            return;
        }
        List filteredCommands = commands.subList((page - 1) * 8, Math.min(page * 8, commands.size()));
        Component header = this.getMessage("helpHeader", "%NAME%", "Maintenance" + (Object)((Object)this.plugin.getServerType()), "%PAGE%", Integer.toString(page), "%MAX%", Integer.toString((commands.size() + this.getDivide(commands.size())) / 8));
        sender.send(Component.empty());
        sender.send(header);
        for (Component command : filteredCommands) {
            sender.send(command);
        }
        if (page * 8 < commands.size()) {
            sender.send(this.getMessage("helpNextPage", "%PAGE%", Integer.toString(page + 1)));
        } else {
            sender.sendRich("<dark_gray>\u00d7 <yellow>Version: " + this.plugin.getVersion() + " <gray>by <aqua>kennytv");
        }
        sender.send(header);
        sender.send(Component.empty());
    }

    private int getDivide(int size) {
        int commandSize = size % 8;
        return commandSize > 0 ? 8 - commandSize : 0;
    }
}

