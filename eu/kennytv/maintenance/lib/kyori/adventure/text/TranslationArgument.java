/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslationArgumentImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslationArgumentLike;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface TranslationArgument
extends TranslationArgumentLike,
Examinable {
    @NotNull
    public static TranslationArgument bool(boolean value) {
        return new TranslationArgumentImpl(value);
    }

    @NotNull
    public static TranslationArgument numeric(@NotNull Number value) {
        return new TranslationArgumentImpl(Objects.requireNonNull(value, "value"));
    }

    @NotNull
    public static TranslationArgument component(@NotNull ComponentLike value) {
        if (value instanceof TranslationArgumentLike) {
            return ((TranslationArgumentLike)value).asTranslationArgument();
        }
        return new TranslationArgumentImpl(Objects.requireNonNull(Objects.requireNonNull(value, "value").asComponent(), "value.asComponent()"));
    }

    @NotNull
    public Object value();

    @Override
    @NotNull
    default public TranslationArgument asTranslationArgument() {
        return this;
    }
}

