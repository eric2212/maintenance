/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.listener;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.NamedTextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextDecoration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class JoinListenerBase {
    protected final MaintenancePlugin plugin;
    protected final Settings settings;
    private final Set<UUID> notifiedPlayers = new HashSet<UUID>();

    protected JoinListenerBase(MaintenancePlugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    protected boolean shouldKick(SenderInfo sender, boolean updateCheck) {
        if (!this.settings.isMaintenance() || sender.hasMaintenancePermission("bypass") || this.settings.isWhitelisted(sender.getUuid())) {
            if (updateCheck) {
                this.updateCheck(sender);
            }
            return false;
        }
        return true;
    }

    protected boolean shouldKick(SenderInfo sender) {
        return this.shouldKick(sender, true);
    }

    protected void updateCheck(SenderInfo sender) {
        if (!this.settings.hasUpdateChecks()) {
            return;
        }
        if (!sender.hasPermission("maintenance.admin") || this.notifiedPlayers.contains(sender.getUuid())) {
            return;
        }
        this.plugin.async(() -> {
            if (!this.plugin.updateAvailable()) {
                return;
            }
            this.notifiedPlayers.add(sender.getUuid());
            sender.sendPrefixedRich("<red>There is a newer version available: <green>Version " + this.plugin.getNewestVersion() + "<red>, you're on <green>" + this.plugin.getVersion());
            TextComponent text = (TextComponent)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)Component.text().content("Download it at: ").color(NamedTextColor.RED)).append((ComponentBuilder<?, ?>)Component.text().content("https://hangar.papermc.io/kennytv/Maintenance").color(NamedTextColor.GOLD))).append((ComponentBuilder<?, ?>)((TextComponent.Builder)Component.text().content(" (CLICK ME)").color(NamedTextColor.GRAY)).decorate(TextDecoration.BOLD))).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://hangar.papermc.io/kennytv/Maintenance"))).hoverEvent(HoverEvent.showText(Component.text("Download the latest version").color(NamedTextColor.GREEN)))).build();
            sender.send(this.plugin.prefix().append(text));
        });
    }

    protected abstract void broadcastJoinNotification(String var1);
}

