/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.StyleBuilderApplicable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.TagPattern;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class Placeholder {
    private Placeholder() {
    }

    public static @NotNull TagResolver.Single parsed(@TagPattern @NotNull String key, @NotNull String value) {
        return TagResolver.resolver(key, Tag.preProcessParsed(value));
    }

    public static @NotNull TagResolver.Single unparsed(@TagPattern @NotNull String key, @NotNull String value) {
        Objects.requireNonNull(value, "value");
        return Placeholder.component(key, Component.text(value));
    }

    public static @NotNull TagResolver.Single component(@TagPattern @NotNull String key, @NotNull ComponentLike value) {
        return TagResolver.resolver(key, Tag.selfClosingInserting(value));
    }

    public static @NotNull TagResolver.Single styling(@TagPattern @NotNull String key, @NotNull @NotNull StyleBuilderApplicable @NotNull ... style) {
        return TagResolver.resolver(key, Tag.styling(style));
    }
}

