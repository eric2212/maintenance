/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Range
 */
package eu.kennytv.maintenance.lib.kyori.adventure.util;

import eu.kennytv.maintenance.lib.kyori.adventure.util.HSVLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface RGBLike {
    public @Range(from=0L, to=255L) int red();

    public @Range(from=0L, to=255L) int green();

    public @Range(from=0L, to=255L) int blue();

    @NotNull
    default public HSVLike asHSV() {
        return HSVLike.fromRGB(this.red(), this.green(), this.blue());
    }
}

