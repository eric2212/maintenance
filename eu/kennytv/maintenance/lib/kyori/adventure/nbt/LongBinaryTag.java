/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagType;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagTypes;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.LongBinaryTagImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.NumberBinaryTag;
import org.jetbrains.annotations.NotNull;

public interface LongBinaryTag
extends NumberBinaryTag {
    @NotNull
    public static LongBinaryTag of(long value) {
        return new LongBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<LongBinaryTag> type() {
        return BinaryTagTypes.LONG;
    }

    public long value();
}

