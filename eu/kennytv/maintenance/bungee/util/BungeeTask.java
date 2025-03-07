/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ProxyServer
 */
package eu.kennytv.maintenance.bungee.util;

import eu.kennytv.maintenance.core.util.Task;
import net.md_5.bungee.api.ProxyServer;

public final class BungeeTask
implements Task {
    private final int id;

    public BungeeTask(int id) {
        this.id = id;
    }

    @Override
    public void cancel() {
        ProxyServer.getInstance().getScheduler().cancel(this.id);
    }
}

