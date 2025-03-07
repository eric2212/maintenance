/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Debug$Renderer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.AbstractBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.DoubleBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.ShadyPines;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(text="String.valueOf(this.value) + \"d\"", hasChildren="false")
final class DoubleBinaryTagImpl
extends AbstractBinaryTag
implements DoubleBinaryTag {
    private final double value;

    DoubleBinaryTagImpl(double value) {
        this.value = value;
    }

    @Override
    public double value() {
        return this.value;
    }

    @Override
    public byte byteValue() {
        return (byte)(ShadyPines.floor(this.value) & 0xFF);
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return (float)this.value;
    }

    @Override
    public int intValue() {
        return ShadyPines.floor(this.value);
    }

    @Override
    public long longValue() {
        return (long)Math.floor(this.value);
    }

    @Override
    public short shortValue() {
        return (short)(ShadyPines.floor(this.value) & 0xFFFF);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        DoubleBinaryTagImpl that = (DoubleBinaryTagImpl)other;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value);
    }

    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

