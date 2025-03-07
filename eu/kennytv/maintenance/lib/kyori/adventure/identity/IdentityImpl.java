/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.identity;

import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identity;
import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class IdentityImpl
implements Examinable,
Identity {
    private final UUID uuid;

    IdentityImpl(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    @NotNull
    public UUID uuid() {
        return this.uuid;
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Identity)) {
            return false;
        }
        Identity that = (Identity)other;
        return this.uuid.equals(that.uuid());
    }

    public int hashCode() {
        return this.uuid.hashCode();
    }
}

