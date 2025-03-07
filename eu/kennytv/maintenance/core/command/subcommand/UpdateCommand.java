/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.NamedTextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.MiniMessage;

public final class UpdateCommand
extends CommandInfo {
    public UpdateCommand(MaintenancePlugin plugin) {
        super(plugin, "update");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (this.checkArgs(sender, args, 1)) {
            return;
        }
        if (args[0].equalsIgnoreCase("update")) {
            this.plugin.async(() -> this.checkForUpdate(sender));
            return;
        }
        if (!this.plugin.updateAvailable()) {
            sender.sendPrefixedRich("<green>You already have the latest version of the plugin!");
            return;
        }
        sender.send(this.getMessage("updateDownloading", new String[0]));
        try {
            if (this.plugin.installUpdate()) {
                sender.send(this.getMessage("updateFinished", new String[0]));
            } else {
                sender.send(this.getMessage("updateFailed", new String[0]));
            }
        } catch (Exception e) {
            sender.send(this.getMessage("updateFailed", new String[0]));
            e.printStackTrace();
        }
    }

    private void checkForUpdate(SenderInfo sender) {
        if (!this.plugin.updateAvailable()) {
            sender.sendPrefixedRich("<green>You already have the latest version of the plugin!");
            return;
        }
        sender.sendPrefixedRich("<red>Newest version available: <green>Version " + this.plugin.getNewestVersion() + "<red>, you're on <green>" + this.plugin.getVersion());
        sender.sendPrefixedRich("<b><red>WARNING: <red>You will have to restart the server to prevent further issues and to complete the update! If you can't do that, don't update!");
        sender.send((Component)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)Component.text().append((Component)MiniMessage.miniMessage().deserialize("<gold>\u00d7 <dark_gray>[<green>Update<dark_gray>]"))).clickEvent(ClickEvent.runCommand("/maintenance forceupdate"))).hoverEvent(HoverEvent.showText((ComponentLike)((Object)Component.text().content("Click here to update the plugin").color(NamedTextColor.GREEN))))).append((ComponentBuilder<?, ?>)Component.text().content(", or manually run ").color(NamedTextColor.GRAY))).append((ComponentBuilder<?, ?>)Component.text().content("/maintenance forceupdate").color(NamedTextColor.RED))).build());
    }
}

