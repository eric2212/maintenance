/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.util;

import java.util.UUID;

public final class ProfileLookup {
    private final UUID uuid;
    private final String name;

    public ProfileLookup(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }
}

