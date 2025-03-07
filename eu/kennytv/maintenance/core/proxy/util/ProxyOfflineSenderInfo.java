/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.util;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.proxy.util.ProxySenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.util.UUID;

public final class ProxyOfflineSenderInfo
implements ProxySenderInfo {
    private final UUID uuid;
    private final String name;

    public ProxyOfflineSenderInfo(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public boolean canAccess(Server server) {
        return false;
    }

    @Override
    public void disconnect(Component component) {
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void send(Component component) {
    }

    @Override
    public boolean isPlayer() {
        return true;
    }
}

