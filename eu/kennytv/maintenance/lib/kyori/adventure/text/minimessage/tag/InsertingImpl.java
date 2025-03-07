/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.AbstractTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Inserting;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class InsertingImpl
extends AbstractTag
implements Inserting {
    private final boolean allowsChildren;
    private final Component value;

    InsertingImpl(boolean allowsChildren, Component value) {
        this.allowsChildren = allowsChildren;
        this.value = value;
    }

    @Override
    public boolean allowsChildren() {
        return this.allowsChildren;
    }

    @Override
    @NotNull
    public Component value() {
        return this.value;
    }

    public int hashCode() {
        return Objects.hash(this.allowsChildren, this.value);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InsertingImpl)) {
            return false;
        }
        InsertingImpl that = (InsertingImpl)other;
        return this.allowsChildren == that.allowsChildren && Objects.equals(this.value, that.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("allowsChildren", this.allowsChildren), ExaminableProperty.of("value", this.value));
    }
}

