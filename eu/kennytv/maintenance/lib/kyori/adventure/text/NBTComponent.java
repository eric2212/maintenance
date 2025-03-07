/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.text.BuildableComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.NBTComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NBTComponent<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>>
extends BuildableComponent<C, B> {
    @NotNull
    public String nbtPath();

    @Contract(pure=true)
    @NotNull
    public C nbtPath(@NotNull String var1);

    public boolean interpret();

    @Contract(pure=true)
    @NotNull
    public C interpret(boolean var1);

    @Nullable
    public Component separator();

    @NotNull
    public C separator(@Nullable ComponentLike var1);

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("nbtPath", this.nbtPath()), ExaminableProperty.of("interpret", this.interpret()), ExaminableProperty.of("separator", this.separator())), BuildableComponent.super.examinableProperties());
    }
}

