/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.proxy.listener;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.listener.JoinListenerBase;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;
import eu.kennytv.maintenance.core.proxy.util.ProxySenderInfo;
import eu.kennytv.maintenance.core.proxy.util.ServerConnectResult;
import org.jetbrains.annotations.Nullable;

public abstract class ProxyJoinListenerBase
extends JoinListenerBase {
    private static final ServerConnectResult ALLOWED = new ServerConnectResult(false);
    private static final ServerConnectResult DENIED = new ServerConnectResult(true);
    protected final MaintenanceProxyPlugin plugin;
    protected final SettingsProxy settings;
    private boolean warned;

    protected ProxyJoinListenerBase(MaintenanceProxyPlugin plugin, SettingsProxy settings) {
        super(plugin, settings);
        this.plugin = plugin;
        this.settings = settings;
    }

    protected ServerConnectResult serverConnect(ProxySenderInfo sender, Server target, boolean normalServerConnect) {
        if (this.settings.isMaintenance()) {
            if (sender.hasMaintenancePermission("bypass") || this.settings.isWhitelisted(sender.getUuid())) {
                return ALLOWED;
            }
            Server waitingServer = this.shouldConnectToWaitingServer(sender);
            if (waitingServer == null) {
                return DENIED;
            }
            if (target.getName().equals(waitingServer.getName())) {
                return ALLOWED;
            }
            String currentServer = this.plugin.getServerNameOf(sender);
            if (waitingServer.getName().equals(currentServer)) {
                sender.send(this.settings.getMessage("forceWaitingServer", new String[0]));
                return DENIED;
            }
            sender.send(this.settings.getMessage("sentToWaitingServer", new String[0]));
            return new ServerConnectResult(waitingServer);
        }
        if (!this.settings.isMaintenance(target.getName())) {
            return ALLOWED;
        }
        if (sender.hasMaintenancePermission("bypass") || this.settings.isWhitelisted(sender.getUuid()) || sender.hasMaintenancePermission("singleserver.bypass." + target.getName().toLowerCase())) {
            return ALLOWED;
        }
        if (this.settings.isJoinNotifications()) {
            this.broadcastJoinNotification(sender.getName(), target);
        }
        if (normalServerConnect) {
            sender.send(this.settings.getServerKickMessage(target.getName()));
            return DENIED;
        }
        Server fallback = this.settings.getFallbackServer();
        if (fallback == null || !sender.canAccess(fallback)) {
            sender.disconnect(this.settings.getFullServerKickMessage(target.getName()));
            if (!this.warned) {
                this.plugin.getLogger().warning("Could not send player to the set fallback server; instead kicking player off the network!");
                this.warned = true;
            }
            return DENIED;
        }
        return new ServerConnectResult(fallback);
    }

    @Nullable
    protected Server shouldConnectToWaitingServer(ProxySenderInfo sender) {
        if (this.settings.getWaitingServer() == null) {
            return null;
        }
        Server waitingServer = this.plugin.getServer(this.settings.getWaitingServer());
        if (waitingServer == null) {
            return null;
        }
        if (!sender.canAccess(waitingServer) || this.settings.isMaintenance(waitingServer.getName())) {
            return null;
        }
        return waitingServer;
    }

    protected abstract void broadcastJoinNotification(String var1, Server var2);
}

