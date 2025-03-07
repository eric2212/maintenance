/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.auto.service.AutoService
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.impl;

import com.google.auto.service.AutoService;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.DataComponentValue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.GsonDataComponentValue;
import java.util.Collections;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@AutoService(value={DataComponentValueConverterRegistry.Provider.class})
@ApiStatus.Internal
public final class GsonDataComponentValueConverterProvider
implements DataComponentValueConverterRegistry.Provider {
    private static final Key ID = Key.key("adventure", "serializer/gson");

    @Override
    @NotNull
    public Key id() {
        return ID;
    }

    @Override
    @NotNull
    public Iterable<DataComponentValueConverterRegistry.Conversion<?, ?>> conversions() {
        return Collections.singletonList(DataComponentValueConverterRegistry.Conversion.convert(DataComponentValue.Removed.class, GsonDataComponentValue.class, (k, removed) -> GsonDataComponentValue.gsonDataComponentValue((JsonElement)JsonNull.INSTANCE)));
    }
}

