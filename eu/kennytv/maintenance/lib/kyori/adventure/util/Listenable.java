/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public abstract class Listenable<L> {
    private final List<L> listeners = new CopyOnWriteArrayList<L>();

    protected final void forEachListener(@NotNull Consumer<L> consumer) {
        for (L listener : this.listeners) {
            consumer.accept(listener);
        }
    }

    protected final void addListener0(@NotNull L listener) {
        this.listeners.add(listener);
    }

    protected final void removeListener0(@NotNull L listener) {
        this.listeners.remove(listener);
    }
}

