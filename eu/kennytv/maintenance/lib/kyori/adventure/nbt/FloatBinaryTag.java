/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagType;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTagTypes;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.FloatBinaryTagImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.NumberBinaryTag;
import org.jetbrains.annotations.NotNull;

public interface FloatBinaryTag
extends NumberBinaryTag {
    @NotNull
    public static FloatBinaryTag of(float value) {
        return new FloatBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<FloatBinaryTag> type() {
        return BinaryTagTypes.FLOAT;
    }

    public float value();
}

