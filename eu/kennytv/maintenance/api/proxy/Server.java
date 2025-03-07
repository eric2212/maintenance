/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.api.proxy;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;

public interface Server {
    public String getName();

    public boolean hasPlayers();

    public void broadcast(Component var1);

    public boolean isRegisteredServer();
}

