/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.ByteArrayBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.ByteBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.CompoundBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.DoubleBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.FloatBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.IntArrayBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.IntBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.LongArrayBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.LongBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.ShortBinaryTag;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.StringBinaryTag;
import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CompoundTagSetter<R> {
    @NotNull
    public R put(@NotNull String var1, @NotNull BinaryTag var2);

    @NotNull
    public R put(@NotNull CompoundBinaryTag var1);

    @NotNull
    public R put(@NotNull Map<String, ? extends BinaryTag> var1);

    @NotNull
    default public R remove(@NotNull String key) {
        return this.remove(key, null);
    }

    @NotNull
    public R remove(@NotNull String var1, @Nullable Consumer<? super BinaryTag> var2);

    @NotNull
    default public R putBoolean(@NotNull String key, boolean value) {
        return this.put(key, value ? ByteBinaryTag.ONE : ByteBinaryTag.ZERO);
    }

    @NotNull
    default public R putByte(@NotNull String key, byte value) {
        return this.put(key, ByteBinaryTag.of(value));
    }

    @NotNull
    default public R putShort(@NotNull String key, short value) {
        return this.put(key, ShortBinaryTag.of(value));
    }

    @NotNull
    default public R putInt(@NotNull String key, int value) {
        return this.put(key, IntBinaryTag.of(value));
    }

    @NotNull
    default public R putLong(@NotNull String key, long value) {
        return this.put(key, LongBinaryTag.of(value));
    }

    @NotNull
    default public R putFloat(@NotNull String key, float value) {
        return this.put(key, FloatBinaryTag.of(value));
    }

    @NotNull
    default public R putDouble(@NotNull String key, double value) {
        return this.put(key, DoubleBinaryTag.of(value));
    }

    @NotNull
    default public R putByteArray(@NotNull String key, byte @NotNull [] value) {
        return this.put(key, ByteArrayBinaryTag.of(value));
    }

    @NotNull
    default public R putString(@NotNull String key, @NotNull String value) {
        return this.put(key, StringBinaryTag.of(value));
    }

    @NotNull
    default public R putIntArray(@NotNull String key, int @NotNull [] value) {
        return this.put(key, IntArrayBinaryTag.of(value));
    }

    @NotNull
    default public R putLongArray(@NotNull String key, long @NotNull [] value) {
        return this.put(key, LongArrayBinaryTag.of(value));
    }
}

