/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.internal;

import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import eu.kennytv.maintenance.lib.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class Internals {
    private Internals() {
    }

    @NotNull
    public static String toString(@NotNull Examinable examinable) {
        return examinable.examine(StringExaminer.simpleEscaping());
    }
}

