/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 */
package eu.kennytv.maintenance.paper.util;

import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.util.UUID;
import org.bukkit.OfflinePlayer;

public final class BukkitOfflinePlayerInfo
implements SenderInfo {
    private final OfflinePlayer player;

    public BukkitOfflinePlayerInfo(OfflinePlayer player) {
        this.player = player;
    }

    @Override
    public UUID getUuid() {
        return this.player.getUniqueId();
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void send(Component component) {
    }

    @Override
    public boolean isPlayer() {
        return true;
    }
}

