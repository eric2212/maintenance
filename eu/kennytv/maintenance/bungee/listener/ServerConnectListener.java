/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.CommandSender
 *  net.md_5.bungee.api.ProxyServer
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 *  net.md_5.bungee.api.event.ServerConnectEvent
 *  net.md_5.bungee.api.event.ServerConnectEvent$Reason
 *  net.md_5.bungee.api.plugin.Listener
 *  net.md_5.bungee.event.EventHandler
 */
package eu.kennytv.maintenance.bungee.listener;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.bungee.MaintenanceBungeePlugin;
import eu.kennytv.maintenance.bungee.util.BungeeSenderInfo;
import eu.kennytv.maintenance.bungee.util.BungeeServer;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;
import eu.kennytv.maintenance.core.proxy.listener.ProxyJoinListenerBase;
import eu.kennytv.maintenance.core.proxy.util.ServerConnectResult;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class ServerConnectListener
extends ProxyJoinListenerBase
implements Listener {
    private final MaintenanceBungeePlugin plugin;

    public ServerConnectListener(MaintenanceBungeePlugin plugin, SettingsProxy settings) {
        super(plugin, settings);
        this.plugin = plugin;
    }

    @EventHandler
    public void initialServerConnect(ServerConnectEvent event) {
        if (event.isCancelled() || event.getReason() != ServerConnectEvent.Reason.JOIN_PROXY) {
            return;
        }
        BungeeSenderInfo player = new BungeeSenderInfo((CommandSender)event.getPlayer());
        if (this.plugin.isDebug()) {
            this.plugin.getLogger().info("Join permission check for " + event.getPlayer().getName() + " - Permission: " + player.hasMaintenancePermission("bypass") + ", whitelist: " + this.settings.isWhitelisted(player.getUuid()));
        }
        if (this.shouldKick(player)) {
            Server waitingServer = this.shouldConnectToWaitingServer(player);
            if (waitingServer != null) {
                event.setTarget(((BungeeServer)waitingServer).getServer());
                player.send(this.settings.getMessage("sentToWaitingServer", new String[0]));
                if (this.plugin.isDebug()) {
                    this.plugin.getLogger().info("Join to waiting server for " + event.getPlayer().getName());
                }
                return;
            }
            event.setCancelled(true);
            player.disconnect(this.settings.getKickMessage());
            if (this.settings.isJoinNotifications()) {
                this.broadcastJoinNotification(player.getName());
            }
        }
    }

    @EventHandler(priority=32)
    public void serverConnect(ServerConnectEvent event) {
        if (event.isCancelled()) {
            return;
        }
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        boolean normalServerConnect = event.getReason() != ServerConnectEvent.Reason.JOIN_PROXY && event.getReason() != ServerConnectEvent.Reason.KICK_REDIRECT && event.getReason() != ServerConnectEvent.Reason.LOBBY_FALLBACK && event.getReason() != ServerConnectEvent.Reason.SERVER_DOWN_REDIRECT;
        BungeeSenderInfo player = new BungeeSenderInfo((CommandSender)proxiedPlayer);
        ServerConnectResult connectResult = this.serverConnect(player, new BungeeServer(event.getTarget()), normalServerConnect);
        if (this.plugin.isDebug()) {
            this.plugin.getLogger().info("Connectresult for " + player.getName() + " to " + event.getTarget().getName() + ": " + connectResult);
        }
        if (connectResult.isCancelled()) {
            event.setCancelled(true);
            if (proxiedPlayer.getServer() == null) {
                player.disconnect(this.settings.getKickMessage());
            }
        } else if (connectResult.getTarget() != null) {
            event.setTarget(((BungeeServer)connectResult.getTarget()).getServer());
        }
    }

    @Override
    protected void broadcastJoinNotification(String name) {
        this.sendJoinMessage(ProxyServer.getInstance().getPlayers(), name);
    }

    @Override
    protected void broadcastJoinNotification(String name, Server server) {
        this.sendJoinMessage(((BungeeServer)server).getServer().getPlayers(), name);
    }

    private void sendJoinMessage(Iterable<ProxiedPlayer> players, String name) {
        Component component = this.settings.getMessage("joinNotification", "%PLAYER%", name);
        for (ProxiedPlayer player : players) {
            if (!this.plugin.hasPermission((CommandSender)player, "joinnotification")) continue;
            this.plugin.audiences().player(player).sendMessage(component);
        }
    }
}

