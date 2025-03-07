/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.event;

import eu.kennytv.maintenance.lib.kyori.adventure.nbt.api.BinaryTagHolder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.RemovedDataComponentValueImpl;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;

public interface DataComponentValue
extends Examinable {
    public static @NotNull Removed removed() {
        return RemovedDataComponentValueImpl.REMOVED;
    }

    public static interface Removed
    extends DataComponentValue {
    }

    public static interface TagSerializable
    extends DataComponentValue {
        @NotNull
        public BinaryTagHolder asBinaryTag();
    }
}

