/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.NamedTextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import java.io.IOException;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TextColorSerializer
extends TypeAdapter<TextColor> {
    static final TypeAdapter<TextColor> INSTANCE = new TextColorSerializer(false).nullSafe();
    static final TypeAdapter<TextColor> DOWNSAMPLE_COLOR = new TextColorSerializer(true).nullSafe();
    private final boolean downsampleColor;

    private TextColorSerializer(boolean downsampleColor) {
        this.downsampleColor = downsampleColor;
    }

    public void write(JsonWriter out, TextColor value) throws IOException {
        if (value instanceof NamedTextColor) {
            out.value(NamedTextColor.NAMES.key((NamedTextColor)value));
        } else if (this.downsampleColor) {
            out.value(NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)));
        } else {
            out.value(TextColorSerializer.asUpperCaseHexString(value));
        }
    }

    private static String asUpperCaseHexString(TextColor color) {
        return String.format(Locale.ROOT, "#%06X", color.value());
    }

    @Nullable
    public TextColor read(JsonReader in) throws IOException {
        @Nullable TextColor color = TextColorSerializer.fromString(in.nextString());
        if (color == null) {
            return null;
        }
        return this.downsampleColor ? NamedTextColor.nearestTo(color) : color;
    }

    @Nullable
    static TextColor fromString(@NotNull String value) {
        if (value.startsWith("#")) {
            return TextColor.fromHexString(value);
        }
        return NamedTextColor.NAMES.value(value);
    }
}

