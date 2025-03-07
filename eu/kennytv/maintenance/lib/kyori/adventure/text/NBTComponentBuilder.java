/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.NBTComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>>
extends ComponentBuilder<C, B> {
    @Contract(value="_ -> this")
    @NotNull
    public B nbtPath(@NotNull String var1);

    @Contract(value="_ -> this")
    @NotNull
    public B interpret(boolean var1);

    @Contract(value="_ -> this")
    @NotNull
    public B separator(@Nullable ComponentLike var1);
}

