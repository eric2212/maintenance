/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer;

import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class StyleClaimImpl<V>
implements StyleClaim<V> {
    private final String claimKey;
    private final Function<Style, V> lens;
    private final Predicate<V> filter;
    private final BiConsumer<V, TokenEmitter> emitable;

    StyleClaimImpl(String claimKey, Function<Style, @Nullable V> lens, Predicate<V> filter, BiConsumer<V, TokenEmitter> emitable) {
        this.claimKey = claimKey;
        this.lens = lens;
        this.filter = filter;
        this.emitable = emitable;
    }

    @Override
    @NotNull
    public String claimKey() {
        return this.claimKey;
    }

    @Override
    @Nullable
    public Emitable apply(@NotNull Style style) {
        V element = this.lens.apply(style);
        if (element == null || !this.filter.test(element)) {
            return null;
        }
        return emitter -> this.emitable.accept(element, emitter);
    }

    public int hashCode() {
        return Objects.hash(this.claimKey);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StyleClaimImpl)) {
            return false;
        }
        StyleClaimImpl that = (StyleClaimImpl)other;
        return Objects.equals(this.claimKey, that.claimKey);
    }
}

