/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package eu.kennytv.maintenance.paper.util;

import eu.kennytv.maintenance.api.MaintenanceProvider;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.paper.MaintenancePaperPlugin;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class BukkitSenderInfo
implements SenderInfo {
    private final CommandSender sender;

    public BukkitSenderInfo(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public UUID getUuid() {
        return this.sender instanceof Player ? ((Entity)this.sender).getUniqueId() : null;
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
        ((MaintenancePaperPlugin)MaintenanceProvider.get()).audiences().sender(this.sender).sendMessage(component);
    }

    @Override
    public boolean isPlayer() {
        return this.sender instanceof Player;
    }
}

