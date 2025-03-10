/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Range
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.format;

import eu.kennytv.maintenance.lib.kyori.adventure.text.format.NamedTextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.StyleBuilderApplicable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColorImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextFormat;
import eu.kennytv.maintenance.lib.kyori.adventure.util.HSVLike;
import eu.kennytv.maintenance.lib.kyori.adventure.util.RGBLike;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public interface TextColor
extends Comparable<TextColor>,
Examinable,
RGBLike,
StyleBuilderApplicable,
TextFormat {
    public static final char HEX_CHARACTER = '#';
    public static final String HEX_PREFIX = "#";

    @NotNull
    public static TextColor color(int value) {
        int truncatedValue = value & 0xFFFFFF;
        NamedTextColor named = NamedTextColor.namedColor(truncatedValue);
        return named != null ? named : new TextColorImpl(truncatedValue);
    }

    @NotNull
    public static TextColor color(@NotNull RGBLike rgb) {
        if (rgb instanceof TextColor) {
            return (TextColor)rgb;
        }
        return TextColor.color(rgb.red(), rgb.green(), rgb.blue());
    }

    @NotNull
    public static TextColor color(@NotNull HSVLike hsv) {
        float s = hsv.s();
        float v = hsv.v();
        if (s == 0.0f) {
            return TextColor.color(v, v, v);
        }
        float h = hsv.h() * 6.0f;
        int i = (int)Math.floor(h);
        float f = h - (float)i;
        float p = v * (1.0f - s);
        float q = v * (1.0f - s * f);
        float t = v * (1.0f - s * (1.0f - f));
        if (i == 0) {
            return TextColor.color(v, t, p);
        }
        if (i == 1) {
            return TextColor.color(q, v, p);
        }
        if (i == 2) {
            return TextColor.color(p, v, t);
        }
        if (i == 3) {
            return TextColor.color(p, q, v);
        }
        if (i == 4) {
            return TextColor.color(t, p, v);
        }
        return TextColor.color(v, p, q);
    }

    @NotNull
    public static TextColor color(@Range(from=0L, to=255L) int r, @Range(from=0L, to=255L) int g, @Range(from=0L, to=255L) int b) {
        return TextColor.color((r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF);
    }

    @NotNull
    public static TextColor color(float r, float g, float b) {
        return TextColor.color((int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
    }

    @Nullable
    public static TextColor fromHexString(@NotNull String string) {
        if (string.startsWith(HEX_PREFIX)) {
            try {
                int hex = Integer.parseInt(string.substring(1), 16);
                return TextColor.color(hex);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Nullable
    public static TextColor fromCSSHexString(@NotNull String string) {
        if (string.startsWith(HEX_PREFIX)) {
            int hex;
            String hexString = string.substring(1);
            if (hexString.length() != 3 && hexString.length() != 6) {
                return null;
            }
            try {
                hex = Integer.parseInt(hexString, 16);
            } catch (NumberFormatException e) {
                return null;
            }
            if (hexString.length() == 6) {
                return TextColor.color(hex);
            }
            int red = (hex & 0xF00) >> 8 | (hex & 0xF00) >> 4;
            int green = (hex & 0xF0) >> 4 | hex & 0xF0;
            int blue = (hex & 0xF) << 4 | hex & 0xF;
            return TextColor.color(red, green, blue);
        }
        return null;
    }

    public int value();

    @NotNull
    default public String asHexString() {
        return String.format("%c%06x", Character.valueOf('#'), this.value());
    }

    @Override
    default public @Range(from=0L, to=255L) int red() {
        return this.value() >> 16 & 0xFF;
    }

    @Override
    default public @Range(from=0L, to=255L) int green() {
        return this.value() >> 8 & 0xFF;
    }

    @Override
    default public @Range(from=0L, to=255L) int blue() {
        return this.value() & 0xFF;
    }

    @NotNull
    public static TextColor lerp(float t, @NotNull RGBLike a, @NotNull RGBLike b) {
        float clampedT = Math.min(1.0f, Math.max(0.0f, t));
        int ar = a.red();
        int br = b.red();
        int ag = a.green();
        int bg = b.green();
        int ab = a.blue();
        int bb = b.blue();
        return TextColor.color(Math.round((float)ar + clampedT * (float)(br - ar)), Math.round((float)ag + clampedT * (float)(bg - ag)), Math.round((float)ab + clampedT * (float)(bb - ab)));
    }

    @NotNull
    public static <C extends TextColor> C nearestColorTo(@NotNull List<C> values, @NotNull TextColor any) {
        Objects.requireNonNull(any, "color");
        float matchedDistance = Float.MAX_VALUE;
        TextColor match = (TextColor)values.get(0);
        int length = values.size();
        for (int i = 0; i < length; ++i) {
            TextColor potential = (TextColor)values.get(i);
            float distance = TextColorImpl.distance(any.asHSV(), potential.asHSV());
            if (distance < matchedDistance) {
                match = potential;
                matchedDistance = distance;
            }
            if (distance == 0.0f) break;
        }
        return (C)match;
    }

    @Override
    default public void styleApply(@NotNull Style.Builder style) {
        style.color(this);
    }

    @Override
    default public int compareTo(TextColor that) {
        return Integer.compare(this.value(), that.value());
    }

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.asHexString()));
    }
}

