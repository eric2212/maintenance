/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.proxy.util;

import eu.kennytv.maintenance.api.proxy.Server;
import org.jetbrains.annotations.Nullable;

public final class ServerConnectResult {
    private final boolean cancel;
    private final Server target;

    public ServerConnectResult(boolean cancel) {
        this.cancel = cancel;
        this.target = null;
    }

    public ServerConnectResult(Server target) {
        this.cancel = false;
        this.target = target;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    @Nullable
    public Server getTarget() {
        return this.target;
    }

    public String toString() {
        return "ServerConnectResult{cancel=" + this.cancel + ", target=" + this.target + '}';
    }
}

