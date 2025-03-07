/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.craftbukkit;

import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

@Deprecated
public final class BukkitComponentSerializer {
    private BukkitComponentSerializer() {
    }

    @NotNull
    public static LegacyComponentSerializer legacy() {
        return eu.kennytv.maintenance.lib.kyori.adventure.platform.bukkit.BukkitComponentSerializer.legacy();
    }

    @NotNull
    public static GsonComponentSerializer gson() {
        return eu.kennytv.maintenance.lib.kyori.adventure.platform.bukkit.BukkitComponentSerializer.gson();
    }
}

