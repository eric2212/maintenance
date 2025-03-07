/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.DataComponentValue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.GsonDataComponentValueImpl;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface GsonDataComponentValue
extends DataComponentValue {
    public static GsonDataComponentValue gsonDataComponentValue(@NotNull JsonElement data) {
        if (data instanceof JsonNull) {
            return GsonDataComponentValueImpl.RemovedGsonComponentValueImpl.INSTANCE;
        }
        return new GsonDataComponentValueImpl(Objects.requireNonNull(data, "data"));
    }

    @NotNull
    public JsonElement element();
}

