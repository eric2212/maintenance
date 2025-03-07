/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 */
package eu.kennytv.maintenance.paper.listener;

import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.listener.JoinListenerBase;
import eu.kennytv.maintenance.paper.MaintenancePaperPlugin;
import eu.kennytv.maintenance.paper.util.BukkitSenderInfo;
import eu.kennytv.maintenance.paper.util.ComponentUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public final class PlayerLoginListener
extends JoinListenerBase
implements Listener {
    private final MaintenancePaperPlugin plugin;

    public PlayerLoginListener(MaintenancePaperPlugin plugin, Settings settings) {
        super(plugin, settings);
        this.plugin = plugin;
    }

    @EventHandler
    public void postLogin(PlayerLoginEvent event) {
        BukkitSenderInfo sender = new BukkitSenderInfo((CommandSender)event.getPlayer());
        if (this.shouldKick(sender)) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            if (ComponentUtil.PAPER) {
                event.kickMessage(ComponentUtil.toPaperComponent(this.settings.getKickMessage()));
            } else {
                event.setKickMessage(ComponentUtil.toLegacy(this.settings.getKickMessage()));
            }
            if (this.settings.isJoinNotifications()) {
                this.broadcastJoinNotification(sender.getName());
            }
        }
    }

    @Override
    protected void broadcastJoinNotification(String name) {
        for (Player p : this.plugin.getServer().getOnlinePlayers()) {
            if (!this.plugin.hasPermission((CommandSender)p, "joinnotification")) continue;
            this.plugin.audiences().player(p).sendMessage(this.settings.getMessage("joinNotification", "%PLAYER%", name));
        }
    }
}

