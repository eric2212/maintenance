/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.TagInternals;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.ComponentClaimingResolverImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.StyleClaimingResolverImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SerializableResolver {
    @NotNull
    public static TagResolver claimingComponent(@NotNull String name, @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull Function<Component, @Nullable Emitable> componentClaim) {
        return SerializableResolver.claimingComponent(Collections.singleton(name), handler, componentClaim);
    }

    @NotNull
    public static TagResolver claimingComponent(@NotNull Set<String> names, @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull Function<Component, @Nullable Emitable> componentClaim) {
        HashSet<String> ownNames = new HashSet<String>(names);
        for (String name : ownNames) {
            TagInternals.assertValidTagName(name);
        }
        Objects.requireNonNull(handler, "handler");
        return new ComponentClaimingResolverImpl(ownNames, handler, componentClaim);
    }

    @NotNull
    public static TagResolver claimingStyle(@NotNull String name, @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull StyleClaim<?> styleClaim) {
        return SerializableResolver.claimingStyle(Collections.singleton(name), handler, styleClaim);
    }

    @NotNull
    public static TagResolver claimingStyle(@NotNull Set<String> names, @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull StyleClaim<?> styleClaim) {
        HashSet<String> ownNames = new HashSet<String>(names);
        for (String name : ownNames) {
            TagInternals.assertValidTagName(name);
        }
        Objects.requireNonNull(handler, "handler");
        return new StyleClaimingResolverImpl(ownNames, handler, styleClaim);
    }

    public void handle(@NotNull Component var1, @NotNull ClaimConsumer var2);

    public static interface Single
    extends SerializableResolver {
        @Override
        default public void handle(@NotNull Component serializable, @NotNull ClaimConsumer consumer) {
            Emitable component;
            Emitable applied;
            @Nullable StyleClaim<?> style = this.claimStyle();
            if (style != null && !consumer.styleClaimed(style.claimKey()) && (applied = style.apply(serializable.style())) != null) {
                consumer.style(style.claimKey(), applied);
            }
            if (!consumer.componentClaimed() && (component = this.claimComponent(serializable)) != null) {
                consumer.component(component);
            }
        }

        @Nullable
        default public StyleClaim<?> claimStyle() {
            return null;
        }

        @Nullable
        default public Emitable claimComponent(@NotNull Component component) {
            return null;
        }
    }
}

