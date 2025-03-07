/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import java.io.IOException;

final class KeySerializer
extends TypeAdapter<Key> {
    static final TypeAdapter<Key> INSTANCE = new KeySerializer().nullSafe();

    private KeySerializer() {
    }

    public void write(JsonWriter out, Key value) throws IOException {
        out.value(value.asString());
    }

    public Key read(JsonReader in) throws IOException {
        return Key.key(in.nextString());
    }
}

