/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.flattener;

import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FlattenerListener {
    default public void pushStyle(@NotNull Style style) {
    }

    public void component(@NotNull String var1);

    default public boolean shouldContinue() {
        return true;
    }

    default public void popStyle(@NotNull Style style) {
    }
}

