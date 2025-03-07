/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.AbstractTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.PreProcess;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PreProcessTagImpl
extends AbstractTag
implements PreProcess {
    private final String value;

    PreProcessTagImpl(String value) {
        this.value = value;
    }

    @Override
    @NotNull
    public String value() {
        return this.value;
    }

    public int hashCode() {
        return Objects.hash(this.value);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PreProcessTagImpl)) {
            return false;
        }
        PreProcessTagImpl that = (PreProcessTagImpl)other;
        return Objects.equals(this.value, that.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

