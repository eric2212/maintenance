/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.IndexedSerializer;

final class ClickEventActionSerializer {
    static final TypeAdapter<ClickEvent.Action> INSTANCE = IndexedSerializer.lenient("click action", ClickEvent.Action.NAMES);

    private ClickEventActionSerializer() {
    }
}

