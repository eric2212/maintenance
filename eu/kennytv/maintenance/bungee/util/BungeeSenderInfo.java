/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.CommandSender
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.md_5.bungee.api.connection.Connection
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 */
package eu.kennytv.maintenance.bungee.util;

import eu.kennytv.maintenance.api.MaintenanceProvider;
import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.bungee.MaintenanceBungeePlugin;
import eu.kennytv.maintenance.bungee.util.BungeeServer;
import eu.kennytv.maintenance.bungee.util.ComponentUtil;
import eu.kennytv.maintenance.core.proxy.util.ProxySenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.util.UUID;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class BungeeSenderInfo
implements ProxySenderInfo {
    private final CommandSender sender;

    public BungeeSenderInfo(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public UUID getUuid() {
        return this.sender instanceof ProxiedPlayer ? ((ProxiedPlayer)this.sender).getUniqueId() : null;
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public void send(Component component) {
        ((MaintenanceBungeePlugin)MaintenanceProvider.get()).audiences().sender(this.sender).sendMessage(component);
    }

    @Override
    public boolean isPlayer() {
        return this.sender instanceof ProxiedPlayer;
    }

    public void sendMessage(TextComponent textComponent) {
        this.sender.sendMessage((BaseComponent)textComponent);
    }

    @Override
    public boolean canAccess(Server server) {
        return ((BungeeServer)server).getServer().canAccess(this.sender);
    }

    @Override
    public void disconnect(Component component) {
        if (this.sender instanceof Connection) {
            ((Connection)this.sender).disconnect(ComponentUtil.toBadComponent(component));
        }
    }
}

