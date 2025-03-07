/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public final class ForwardingIterator<T>
implements Iterable<T> {
    private final Supplier<Iterator<T>> iterator;
    private final Supplier<Spliterator<T>> spliterator;

    public ForwardingIterator(@NotNull Supplier<Iterator<T>> iterator, @NotNull Supplier<Spliterator<T>> spliterator) {
        this.iterator = Objects.requireNonNull(iterator, "iterator");
        this.spliterator = Objects.requireNonNull(spliterator, "spliterator");
    }

    @Override
    @NotNull
    public Iterator<T> iterator() {
        return this.iterator.get();
    }

    @Override
    @NotNull
    public Spliterator<T> spliterator() {
        return this.spliterator.get();
    }
}

