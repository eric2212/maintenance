/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslationArgument;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TranslationArgumentLike
extends ComponentLike {
    @NotNull
    public TranslationArgument asTranslationArgument();

    @Override
    @NotNull
    default public Component asComponent() {
        return this.asTranslationArgument().asComponent();
    }
}

