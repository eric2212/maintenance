/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.craftbukkit;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

@Deprecated
public final class MinecraftComponentSerializer
implements ComponentSerializer<Component, Component, Object> {
    private static final MinecraftComponentSerializer INSTANCE = new MinecraftComponentSerializer();
    private final eu.kennytv.maintenance.lib.kyori.adventure.platform.bukkit.MinecraftComponentSerializer realSerial = eu.kennytv.maintenance.lib.kyori.adventure.platform.bukkit.MinecraftComponentSerializer.get();

    public static boolean isSupported() {
        return eu.kennytv.maintenance.lib.kyori.adventure.platform.bukkit.MinecraftComponentSerializer.isSupported();
    }

    @NotNull
    public static MinecraftComponentSerializer get() {
        return INSTANCE;
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull Object input) {
        return this.realSerial.deserialize(input);
    }

    @Override
    @NotNull
    public Object serialize(@NotNull Component component) {
        return this.realSerial.serialize(component);
    }
}

