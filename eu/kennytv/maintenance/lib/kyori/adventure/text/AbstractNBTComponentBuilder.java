/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.text.AbstractComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.NBTComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.NBTComponentBuilder;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractNBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>>
extends AbstractComponentBuilder<C, B>
implements NBTComponentBuilder<C, B> {
    @Nullable
    protected String nbtPath;
    protected boolean interpret = false;
    @Nullable
    protected Component separator;

    AbstractNBTComponentBuilder() {
    }

    AbstractNBTComponentBuilder(@NotNull C component) {
        super(component);
        this.nbtPath = component.nbtPath();
        this.interpret = component.interpret();
        this.separator = component.separator();
    }

    @Override
    @NotNull
    public B nbtPath(@NotNull String nbtPath) {
        this.nbtPath = Objects.requireNonNull(nbtPath, "nbtPath");
        return (B)this;
    }

    @Override
    @NotNull
    public B interpret(boolean interpret) {
        this.interpret = interpret;
        return (B)this;
    }

    @Override
    @NotNull
    public B separator(@Nullable ComponentLike separator) {
        this.separator = ComponentLike.unbox(separator);
        return (B)this;
    }
}

