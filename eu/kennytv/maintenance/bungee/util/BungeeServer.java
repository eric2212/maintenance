/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.config.ServerInfo
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 */
package eu.kennytv.maintenance.bungee.util;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.bungee.util.ComponentUtil;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class BungeeServer
implements Server {
    private final ServerInfo server;

    public BungeeServer(ServerInfo server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return this.server.getName();
    }

    @Override
    public boolean hasPlayers() {
        return !this.server.getPlayers().isEmpty();
    }

    @Override
    public void broadcast(Component component) {
        BaseComponent[] components = ComponentUtil.toBadComponent(component);
        for (ProxiedPlayer player : this.server.getPlayers()) {
            player.sendMessage(components);
        }
    }

    @Override
    public boolean isRegisteredServer() {
        return true;
    }

    public ServerInfo getServer() {
        return this.server;
    }
}

