/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.ArrayBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagType;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagTypes;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.ByteArrayBinaryTagImpl;
import org.jetbrains.annotations.NotNull;

public interface ByteArrayBinaryTag
extends ArrayBinaryTag,
Iterable<Byte> {
    @NotNull
    public static ByteArrayBinaryTag of(byte @NotNull ... value) {
        return new ByteArrayBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<ByteArrayBinaryTag> type() {
        return BinaryTagTypes.BYTE_ARRAY;
    }

    public byte @NotNull [] value();

    public int size();

    public byte get(int var1);
}

