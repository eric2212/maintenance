/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.event;

import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEvent;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HoverEventSource<V> {
    @Nullable
    public static <V> HoverEvent<V> unbox(@Nullable HoverEventSource<V> source) {
        return source != null ? source.asHoverEvent() : null;
    }

    @NotNull
    default public HoverEvent<V> asHoverEvent() {
        return this.asHoverEvent(UnaryOperator.identity());
    }

    @NotNull
    public HoverEvent<V> asHoverEvent(@NotNull UnaryOperator<V> var1);
}

