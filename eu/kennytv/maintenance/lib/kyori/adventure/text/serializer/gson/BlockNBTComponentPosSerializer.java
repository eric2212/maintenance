/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.BlockNBTComponent;
import java.io.IOException;

final class BlockNBTComponentPosSerializer
extends TypeAdapter<BlockNBTComponent.Pos> {
    static final TypeAdapter<BlockNBTComponent.Pos> INSTANCE = new BlockNBTComponentPosSerializer().nullSafe();

    private BlockNBTComponentPosSerializer() {
    }

    public BlockNBTComponent.Pos read(JsonReader in) throws IOException {
        String string = in.nextString();
        try {
            return BlockNBTComponent.Pos.fromString(string);
        } catch (IllegalArgumentException ex) {
            throw new JsonParseException("Don't know how to turn " + string + " into a Position");
        }
    }

    public void write(JsonWriter out, BlockNBTComponent.Pos value) throws IOException {
        out.value(value.asString());
    }
}

