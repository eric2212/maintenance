/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagType;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagTypes;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.ByteBinaryTagImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.NumberBinaryTag;
import org.jetbrains.annotations.NotNull;

public interface ByteBinaryTag
extends NumberBinaryTag {
    public static final ByteBinaryTag ZERO = new ByteBinaryTagImpl(0);
    public static final ByteBinaryTag ONE = new ByteBinaryTagImpl(1);

    @NotNull
    public static ByteBinaryTag of(byte value) {
        if (value == 0) {
            return ZERO;
        }
        if (value == 1) {
            return ONE;
        }
        return new ByteBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<ByteBinaryTag> type() {
        return BinaryTagTypes.BYTE;
    }

    public byte value();
}

