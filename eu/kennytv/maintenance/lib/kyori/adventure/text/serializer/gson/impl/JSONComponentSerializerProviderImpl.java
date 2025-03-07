/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.auto.service.AutoService
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.impl;

import com.google.auto.service.AutoService;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Services;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
@AutoService(value={JSONComponentSerializer.Provider.class})
public final class JSONComponentSerializerProviderImpl
implements JSONComponentSerializer.Provider,
Services.Fallback {
    @Override
    @NotNull
    public JSONComponentSerializer instance() {
        return GsonComponentSerializer.gson();
    }

    @Override
    @NotNull
    public @NotNull Supplier<@NotNull JSONComponentSerializer.Builder> builder() {
        return GsonComponentSerializer::builder;
    }

    public String toString() {
        return "JSONComponentSerializerProviderImpl[GsonComponentSerializer]";
    }
}

