/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt.api;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.api.BinaryTagHolder;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Codec;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

final class BinaryTagHolderImpl
implements BinaryTagHolder {
    private final String string;

    BinaryTagHolderImpl(String string) {
        this.string = Objects.requireNonNull(string, "string");
    }

    @Override
    @NotNull
    public String string() {
        return this.string;
    }

    @Override
    @NotNull
    public <T, DX extends Exception> T get(@NotNull Codec<T, String, DX, ?> codec) throws DX {
        return codec.decode(this.string);
    }

    public int hashCode() {
        return 31 * this.string.hashCode();
    }

    public boolean equals(Object that) {
        if (!(that instanceof BinaryTagHolderImpl)) {
            return false;
        }
        return this.string.equals(((BinaryTagHolderImpl)that).string);
    }

    public String toString() {
        return this.string;
    }
}

