/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentDecoder<S, O extends Component> {
    @NotNull
    public O deserialize(@NotNull S var1);

    @Contract(value="!null -> !null; null -> null", pure=true)
    @Nullable
    default public O deserializeOrNull(@Nullable S input) {
        return this.deserializeOr(input, null);
    }

    @Contract(value="!null, _ -> !null; null, _ -> param2", pure=true)
    @Nullable
    default public O deserializeOr(@Nullable S input, @Nullable O fallback) {
        if (input == null) {
            return fallback;
        }
        return this.deserialize(input);
    }
}

