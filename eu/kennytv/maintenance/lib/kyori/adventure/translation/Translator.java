/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.translation;

import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslatableComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.util.TriState;
import java.text.MessageFormat;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Translator {
    @Nullable
    public static Locale parseLocale(@NotNull String string) {
        String[] segments = string.split("_", 3);
        int length = segments.length;
        if (length == 1) {
            return new Locale(string);
        }
        if (length == 2) {
            return new Locale(segments[0], segments[1]);
        }
        if (length == 3) {
            return new Locale(segments[0], segments[1], segments[2]);
        }
        return null;
    }

    @NotNull
    public Key name();

    @NotNull
    default public TriState hasAnyTranslations() {
        return TriState.NOT_SET;
    }

    @Nullable
    public MessageFormat translate(@NotNull String var1, @NotNull Locale var2);

    @Nullable
    default public Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        return null;
    }
}

