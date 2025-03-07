/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Unmodifiable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.format;

import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.DecorationMap;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextDecoration;
import java.util.EnumMap;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface StyleGetter {
    @Nullable
    public Key font();

    @Nullable
    public TextColor color();

    default public boolean hasDecoration(@NotNull TextDecoration decoration) {
        return this.decoration(decoration) == TextDecoration.State.TRUE;
    }

    public @NotNull TextDecoration.State decoration(@NotNull TextDecoration var1);

    default public @Unmodifiable @NotNull Map<TextDecoration, TextDecoration.State> decorations() {
        EnumMap<TextDecoration, TextDecoration.State> decorations = new EnumMap<TextDecoration, TextDecoration.State>(TextDecoration.class);
        for (TextDecoration decoration : DecorationMap.DECORATIONS) {
            TextDecoration.State value = this.decoration(decoration);
            decorations.put(decoration, value);
        }
        return decorations;
    }

    @Nullable
    public ClickEvent clickEvent();

    @Nullable
    public HoverEvent<?> hoverEvent();

    @Nullable
    public String insertion();
}

