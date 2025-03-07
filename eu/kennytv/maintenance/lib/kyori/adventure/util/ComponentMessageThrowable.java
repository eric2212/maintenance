/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.util;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface ComponentMessageThrowable {
    @Nullable
    public static Component getMessage(@Nullable Throwable throwable) {
        if (throwable instanceof ComponentMessageThrowable) {
            return ((ComponentMessageThrowable)((Object)throwable)).componentMessage();
        }
        return null;
    }

    @Nullable
    public static Component getOrConvertMessage(@Nullable Throwable throwable) {
        String message;
        if (throwable instanceof ComponentMessageThrowable) {
            return ((ComponentMessageThrowable)((Object)throwable)).componentMessage();
        }
        if (throwable != null && (message = throwable.getMessage()) != null) {
            return Component.text(message);
        }
        return null;
    }

    @Nullable
    public Component componentMessage();
}

