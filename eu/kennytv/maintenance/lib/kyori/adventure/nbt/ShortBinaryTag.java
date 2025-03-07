/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagType;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagTypes;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.NumberBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.ShortBinaryTagImpl;
import org.jetbrains.annotations.NotNull;

public interface ShortBinaryTag
extends NumberBinaryTag {
    @NotNull
    public static ShortBinaryTag of(short value) {
        return new ShortBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<ShortBinaryTag> type() {
        return BinaryTagTypes.SHORT;
    }

    public short value();
}

