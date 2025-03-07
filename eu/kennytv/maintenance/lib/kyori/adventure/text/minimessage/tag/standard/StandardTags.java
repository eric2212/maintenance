/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextDecoration;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.ClickTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.ColorTagResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.DecorationTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.FontTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.GradientTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.HoverTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.InsertionTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.KeybindTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.NbtTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.NewlineTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.RainbowTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.ResetTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.ScoreTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.SelectorTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.TransitionTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.TranslatableFallbackTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.TranslatableTag;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public final class StandardTags {
    private static final TagResolver ALL = TagResolver.builder().resolvers(HoverTag.RESOLVER, ClickTag.RESOLVER, ColorTagResolver.INSTANCE, KeybindTag.RESOLVER, TranslatableTag.RESOLVER, TranslatableFallbackTag.RESOLVER, InsertionTag.RESOLVER, FontTag.RESOLVER, DecorationTag.RESOLVER, GradientTag.RESOLVER, RainbowTag.RESOLVER, ResetTag.RESOLVER, NewlineTag.RESOLVER, TransitionTag.RESOLVER, SelectorTag.RESOLVER, ScoreTag.RESOLVER, NbtTag.RESOLVER).build();

    private StandardTags() {
    }

    @NotNull
    public static TagResolver decorations(@NotNull TextDecoration decoration) {
        return Objects.requireNonNull(DecorationTag.RESOLVERS.get(decoration), "No resolver found for decoration (this should not be possible?)");
    }

    @NotNull
    public static TagResolver decorations() {
        return DecorationTag.RESOLVER;
    }

    @NotNull
    public static TagResolver color() {
        return ColorTagResolver.INSTANCE;
    }

    @NotNull
    public static TagResolver hoverEvent() {
        return HoverTag.RESOLVER;
    }

    @NotNull
    public static TagResolver clickEvent() {
        return ClickTag.RESOLVER;
    }

    @NotNull
    public static TagResolver keybind() {
        return KeybindTag.RESOLVER;
    }

    @NotNull
    public static TagResolver translatable() {
        return TranslatableTag.RESOLVER;
    }

    @NotNull
    public static TagResolver translatableFallback() {
        return TranslatableFallbackTag.RESOLVER;
    }

    @NotNull
    public static TagResolver insertion() {
        return InsertionTag.RESOLVER;
    }

    @NotNull
    public static TagResolver font() {
        return FontTag.RESOLVER;
    }

    @NotNull
    public static TagResolver gradient() {
        return GradientTag.RESOLVER;
    }

    @NotNull
    public static TagResolver rainbow() {
        return RainbowTag.RESOLVER;
    }

    public static TagResolver transition() {
        return TransitionTag.RESOLVER;
    }

    @NotNull
    public static TagResolver reset() {
        return ResetTag.RESOLVER;
    }

    @NotNull
    public static TagResolver newline() {
        return NewlineTag.RESOLVER;
    }

    @NotNull
    public static TagResolver selector() {
        return SelectorTag.RESOLVER;
    }

    @NotNull
    public static TagResolver score() {
        return ScoreTag.RESOLVER;
    }

    @NotNull
    public static TagResolver nbt() {
        return NbtTag.RESOLVER;
    }

    @NotNull
    public static TagResolver defaults() {
        return ALL;
    }

    static Set<String> names(String ... names) {
        return new HashSet<String>(Arrays.asList(names));
    }
}

