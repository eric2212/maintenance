/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslationArgument;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.SerializerFactory;
import java.io.IOException;

final class TranslationArgumentSerializer
extends TypeAdapter<TranslationArgument> {
    private final Gson gson;

    static TypeAdapter<TranslationArgument> create(Gson gson) {
        return new TranslationArgumentSerializer(gson).nullSafe();
    }

    private TranslationArgumentSerializer(Gson gson) {
        this.gson = gson;
    }

    public void write(JsonWriter out, TranslationArgument value) throws IOException {
        Object raw = value.value();
        if (raw instanceof Boolean) {
            out.value((Boolean)raw);
        } else if (raw instanceof Number) {
            out.value((Number)raw);
        } else if (raw instanceof Component) {
            this.gson.toJson(raw, SerializerFactory.COMPONENT_TYPE, out);
        } else {
            throw new IllegalStateException("Unable to serialize translatable argument of type " + raw.getClass() + ": " + raw);
        }
    }

    public TranslationArgument read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case BOOLEAN: {
                return TranslationArgument.bool(in.nextBoolean());
            }
            case NUMBER: {
                return TranslationArgument.numeric((Number)this.gson.fromJson(in, Number.class));
            }
        }
        return TranslationArgument.component((ComponentLike)this.gson.fromJson(in, SerializerFactory.COMPONENT_TYPE));
    }
}

