/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Set;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class StyleClaimingResolverImpl
implements TagResolver,
SerializableResolver.Single {
    @NotNull
    private final Set<String> names;
    @NotNull
    private final BiFunction<ArgumentQueue, Context, Tag> handler;
    @NotNull
    private final StyleClaim<?> styleClaim;

    StyleClaimingResolverImpl(@NotNull Set<String> names, @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull StyleClaim<?> styleClaim) {
        this.names = names;
        this.handler = handler;
        this.styleClaim = styleClaim;
    }

    @Override
    @Nullable
    public Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if (!this.names.contains(name)) {
            return null;
        }
        return this.handler.apply(arguments, ctx);
    }

    @Override
    public boolean has(@NotNull String name) {
        return this.names.contains(name);
    }

    @Override
    @Nullable
    public StyleClaim<?> claimStyle() {
        return this.styleClaim;
    }
}

