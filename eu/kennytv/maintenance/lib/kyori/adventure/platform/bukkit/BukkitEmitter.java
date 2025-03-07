/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 */
package eu.kennytv.maintenance.lib.kyori.adventure.platform.bukkit;

import eu.kennytv.maintenance.lib.kyori.adventure.sound.Sound;
import org.bukkit.entity.Entity;

final class BukkitEmitter
implements Sound.Emitter {
    final Entity entity;

    BukkitEmitter(Entity entity) {
        this.entity = entity;
    }
}

