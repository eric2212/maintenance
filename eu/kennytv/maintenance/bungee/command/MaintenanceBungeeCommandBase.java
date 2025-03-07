/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.CommandSender
 *  net.md_5.bungee.api.plugin.Command
 *  net.md_5.bungee.api.plugin.TabExecutor
 */
package eu.kennytv.maintenance.bungee.command;

import eu.kennytv.maintenance.bungee.util.BungeeSenderInfo;
import eu.kennytv.maintenance.core.command.MaintenanceCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public final class MaintenanceBungeeCommandBase
extends Command
implements TabExecutor {
    private final MaintenanceCommand command;

    public MaintenanceBungeeCommandBase(MaintenanceCommand command) {
        super("maintenance", "maintenance.command", new String[]{"mt"});
        this.command = command;
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("maintenance.admin") || sender.hasPermission("maintenance.command");
    }

    public void execute(CommandSender sender, String[] args) {
        this.command.execute(new BungeeSenderInfo(sender), args);
    }

    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return this.command.getSuggestions(new BungeeSenderInfo(sender), args);
    }
}

