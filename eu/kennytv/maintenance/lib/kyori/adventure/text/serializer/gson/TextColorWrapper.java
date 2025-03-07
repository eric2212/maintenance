/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSyntaxException
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextDecoration;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.TextColorSerializer;
import java.io.IOException;
import org.jetbrains.annotations.Nullable;

final class TextColorWrapper {
    @Nullable
    final TextColor color;
    @Nullable
    final TextDecoration decoration;
    final boolean reset;

    TextColorWrapper(@Nullable TextColor color, @Nullable TextDecoration decoration, boolean reset) {
        this.color = color;
        this.decoration = decoration;
        this.reset = reset;
    }

    static final class Serializer
    extends TypeAdapter<TextColorWrapper> {
        static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void write(JsonWriter out, TextColorWrapper value) {
            throw new JsonSyntaxException("Cannot write TextColorWrapper instances");
        }

        public TextColorWrapper read(JsonReader in) throws IOException {
            boolean reset;
            String input = in.nextString();
            TextColor color = TextColorSerializer.fromString(input);
            TextDecoration decoration = TextDecoration.NAMES.value(input);
            boolean bl = reset = decoration == null && input.equals("reset");
            if (color == null && decoration == null && !reset) {
                throw new JsonParseException("Don't know how to parse " + input + " at " + in.getPath());
            }
            return new TextColorWrapper(color, decoration, reset);
        }
    }
}

