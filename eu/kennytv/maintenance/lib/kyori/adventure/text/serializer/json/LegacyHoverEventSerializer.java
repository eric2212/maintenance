/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.json;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Codec;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public interface LegacyHoverEventSerializer {
    public @NotNull HoverEvent.ShowItem deserializeShowItem(@NotNull Component var1) throws IOException;

    @NotNull
    public Component serializeShowItem(@NotNull HoverEvent.ShowItem var1) throws IOException;

    public @NotNull HoverEvent.ShowEntity deserializeShowEntity(@NotNull Component var1, Codec.Decoder<Component, String, ? extends RuntimeException> var2) throws IOException;

    @NotNull
    public Component serializeShowEntity(@NotNull HoverEvent.ShowEntity var1, Codec.Encoder<Component, String, ? extends RuntimeException> var2) throws IOException;
}

