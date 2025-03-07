/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.pointer;

import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.PointerImpl;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public interface Pointer<V>
extends Examinable {
    @NotNull
    public static <V> Pointer<V> pointer(@NotNull Class<V> type, @NotNull Key key) {
        return new PointerImpl<V>(type, key);
    }

    @NotNull
    public Class<V> type();

    @NotNull
    public Key key();

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("type", this.type()), ExaminableProperty.of("key", this.key()));
    }
}

