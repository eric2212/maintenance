/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.BinaryTag;
import eu.kennytv.maintenance.lib.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBinaryTag
implements BinaryTag {
    AbstractBinaryTag() {
    }

    @Override
    @NotNull
    public final String examinableName() {
        return this.type().toString();
    }

    public final String toString() {
        return this.examine(StringExaminer.simpleEscaping());
    }
}

