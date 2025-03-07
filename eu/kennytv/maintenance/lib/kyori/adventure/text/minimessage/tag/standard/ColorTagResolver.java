/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.format.NamedTextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ColorTagResolver
implements TagResolver,
SerializableResolver.Single {
    private static final String COLOR_3 = "c";
    private static final String COLOR_2 = "colour";
    private static final String COLOR = "color";
    static final TagResolver INSTANCE = new ColorTagResolver();
    private static final StyleClaim<TextColor> STYLE = StyleClaim.claim("color", Style::color, (color, emitter) -> {
        if (color instanceof NamedTextColor) {
            emitter.tag(NamedTextColor.NAMES.key((NamedTextColor)color));
        } else {
            emitter.tag(color.asHexString());
        }
    });
    private static final Map<String, TextColor> COLOR_ALIASES = new HashMap<String, TextColor>();

    private static boolean isColorOrAbbreviation(String name) {
        return name.equals(COLOR) || name.equals(COLOR_2) || name.equals(COLOR_3);
    }

    ColorTagResolver() {
    }

    @Override
    @Nullable
    public Tag resolve(@NotNull String name, @NotNull ArgumentQueue args, @NotNull Context ctx) throws ParsingException {
        if (!this.has(name)) {
            return null;
        }
        String colorName = ColorTagResolver.isColorOrAbbreviation(name) ? args.popOr("Expected to find a color parameter: <name>|#RRGGBB").lowerValue() : name;
        TextColor color = ColorTagResolver.resolveColor(colorName, ctx);
        return Tag.styling(color);
    }

    @NotNull
    static TextColor resolveColor(@NotNull String colorName, @NotNull Context ctx) throws ParsingException {
        TextColor color = COLOR_ALIASES.containsKey(colorName) ? COLOR_ALIASES.get(colorName) : (colorName.charAt(0) == '#' ? TextColor.fromHexString(colorName) : (TextColor)NamedTextColor.NAMES.value(colorName));
        if (color == null) {
            throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colours or hex (#RRGGBB) colors.", colorName));
        }
        return color;
    }

    @Override
    public boolean has(@NotNull String name) {
        return ColorTagResolver.isColorOrAbbreviation(name) || TextColor.fromHexString(name) != null || NamedTextColor.NAMES.value(name) != null || COLOR_ALIASES.containsKey(name);
    }

    @Override
    @Nullable
    public StyleClaim<?> claimStyle() {
        return STYLE;
    }

    static {
        COLOR_ALIASES.put("dark_grey", NamedTextColor.DARK_GRAY);
        COLOR_ALIASES.put("grey", NamedTextColor.GRAY);
    }
}

