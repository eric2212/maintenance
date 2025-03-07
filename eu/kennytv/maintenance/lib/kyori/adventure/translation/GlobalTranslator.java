/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.translation;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import eu.kennytv.maintenance.lib.kyori.adventure.translation.GlobalTranslatorImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.translation.Translator;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface GlobalTranslator
extends Translator,
Examinable {
    @NotNull
    public static GlobalTranslator translator() {
        return GlobalTranslatorImpl.INSTANCE;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static GlobalTranslator get() {
        return GlobalTranslatorImpl.INSTANCE;
    }

    @NotNull
    public static TranslatableComponentRenderer<Locale> renderer() {
        return GlobalTranslatorImpl.INSTANCE.renderer;
    }

    @NotNull
    public static Component render(@NotNull Component component, @NotNull Locale locale) {
        return GlobalTranslator.renderer().render(component, locale);
    }

    @NotNull
    public Iterable<? extends Translator> sources();

    public boolean addSource(@NotNull Translator var1);

    public boolean removeSource(@NotNull Translator var1);
}

