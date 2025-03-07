/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.server;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;

public final class DummyServer
implements Server {
    private final String name;

    public DummyServer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasPlayers() {
        return false;
    }

    @Override
    public void broadcast(Component component) {
    }

    @Override
    public boolean isRegisteredServer() {
        return false;
    }
}

