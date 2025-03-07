/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.examination;

import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import eu.kennytv.maintenance.lib.kyori.examination.Examiner;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public interface Examinable {
    @NotNull
    default public String examinableName() {
        return this.getClass().getSimpleName();
    }

    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.empty();
    }

    @NotNull
    default public <R> R examine(@NotNull Examiner<R> examiner) {
        return examiner.examine(this);
    }
}

