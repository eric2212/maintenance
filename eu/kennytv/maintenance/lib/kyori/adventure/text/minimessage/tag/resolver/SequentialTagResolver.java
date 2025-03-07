/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SequentialTagResolver
implements TagResolver,
SerializableResolver {
    final TagResolver[] resolvers;

    SequentialTagResolver(@NotNull @NotNull TagResolver @NotNull [] resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    @Nullable
    public Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        ParsingException thrown = null;
        for (TagResolver resolver : this.resolvers) {
            try {
                @Nullable Tag placeholder = resolver.resolve(name, arguments, ctx);
                if (placeholder == null) continue;
                return placeholder;
            } catch (ParsingException ex) {
                arguments.reset();
                if (thrown == null) {
                    thrown = ex;
                    continue;
                }
                thrown.addSuppressed(ex);
            } catch (Exception ex) {
                arguments.reset();
                ParsingException err = ctx.newException("Exception thrown while parsing <" + name + ">", ex, arguments);
                if (thrown == null) {
                    thrown = err;
                    continue;
                }
                thrown.addSuppressed(err);
            }
        }
        if (thrown != null) {
            throw thrown;
        }
        return null;
    }

    @Override
    public boolean has(@NotNull String name) {
        for (TagResolver resolver : this.resolvers) {
            if (!resolver.has(name)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void handle(@NotNull Component serializable, @NotNull ClaimConsumer consumer) {
        for (TagResolver resolver : this.resolvers) {
            if (!(resolver instanceof SerializableResolver)) continue;
            ((SerializableResolver)((Object)resolver)).handle(serializable, consumer);
        }
    }

    public boolean equals(@Nullable Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SequentialTagResolver)) {
            return false;
        }
        SequentialTagResolver that = (SequentialTagResolver)other;
        return Arrays.equals(this.resolvers, that.resolvers);
    }

    public int hashCode() {
        return Arrays.hashCode(this.resolvers);
    }
}

