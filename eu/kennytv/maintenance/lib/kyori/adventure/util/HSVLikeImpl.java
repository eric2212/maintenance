/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.util;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.util.HSVLike;
import eu.kennytv.maintenance.lib.kyori.adventure.util.ShadyPines;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

final class HSVLikeImpl
implements HSVLike {
    private final float h;
    private final float s;
    private final float v;

    HSVLikeImpl(float h, float s, float v) {
        HSVLikeImpl.requireInsideRange(h, "h");
        HSVLikeImpl.requireInsideRange(s, "s");
        HSVLikeImpl.requireInsideRange(v, "v");
        this.h = h;
        this.s = s;
        this.v = v;
    }

    @Override
    public float h() {
        return this.h;
    }

    @Override
    public float s() {
        return this.s;
    }

    @Override
    public float v() {
        return this.v;
    }

    private static void requireInsideRange(float number, String name) throws IllegalArgumentException {
        if (number < 0.0f || 1.0f < number) {
            throw new IllegalArgumentException(name + " (" + number + ") is not inside the required range: [0,1]");
        }
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HSVLikeImpl)) {
            return false;
        }
        HSVLikeImpl that = (HSVLikeImpl)other;
        return ShadyPines.equals(that.h, this.h) && ShadyPines.equals(that.s, this.s) && ShadyPines.equals(that.v, this.v);
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.h), Float.valueOf(this.s), Float.valueOf(this.v));
    }

    public String toString() {
        return Internals.toString(this);
    }
}

