/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagType;
import org.jetbrains.annotations.NotNull;

public interface NumberBinaryTag
extends BinaryTag {
    @NotNull
    public BinaryTagType<? extends NumberBinaryTag> type();

    public byte byteValue();

    public double doubleValue();

    public float floatValue();

    public int intValue();

    public long longValue();

    public short shortValue();
}

