/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonParseException
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.api.BinaryTagHolder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.SerializerFactory;
import java.io.IOException;

final class ShowItemSerializer
extends TypeAdapter<HoverEvent.ShowItem> {
    static final String ID = "id";
    static final String COUNT = "count";
    static final String TAG = "tag";
    private final Gson gson;

    static TypeAdapter<HoverEvent.ShowItem> create(Gson gson) {
        return new ShowItemSerializer(gson).nullSafe();
    }

    private ShowItemSerializer(Gson gson) {
        this.gson = gson;
    }

    public HoverEvent.ShowItem read(JsonReader in) throws IOException {
        in.beginObject();
        Key key = null;
        int count = 1;
        BinaryTagHolder nbt = null;
        while (in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals(ID)) {
                key = (Key)this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
                continue;
            }
            if (fieldName.equals(COUNT)) {
                count = in.nextInt();
                continue;
            }
            if (fieldName.equals(TAG)) {
                JsonToken token = in.peek();
                if (token == JsonToken.STRING || token == JsonToken.NUMBER) {
                    nbt = BinaryTagHolder.binaryTagHolder(in.nextString());
                    continue;
                }
                if (token == JsonToken.BOOLEAN) {
                    nbt = BinaryTagHolder.binaryTagHolder(String.valueOf(in.nextBoolean()));
                    continue;
                }
                if (token == JsonToken.NULL) {
                    in.nextNull();
                    continue;
                }
                throw new JsonParseException("Expected tag to be a string");
            }
            in.skipValue();
        }
        if (key == null) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
        }
        in.endObject();
        return HoverEvent.ShowItem.of(key, count, nbt);
    }

    public void write(JsonWriter out, HoverEvent.ShowItem value) throws IOException {
        BinaryTagHolder nbt;
        out.beginObject();
        out.name(ID);
        this.gson.toJson((Object)value.item(), SerializerFactory.KEY_TYPE, out);
        int count = value.count();
        if (count != 1) {
            out.name(COUNT);
            out.value((long)count);
        }
        if ((nbt = value.nbt()) != null) {
            out.name(TAG);
            out.value(nbt.string());
        }
        out.endObject();
    }
}

