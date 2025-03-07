/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.api.event.proxy;

import eu.kennytv.maintenance.api.event.manager.MaintenanceEvent;
import eu.kennytv.maintenance.api.proxy.Server;

public final class ServerMaintenanceChangedEvent
implements MaintenanceEvent {
    private final Server server;
    private final boolean maintenance;

    public ServerMaintenanceChangedEvent(Server server, boolean maintenance) {
        this.server = server;
        this.maintenance = maintenance;
    }

    public Server getServer() {
        return this.server;
    }

    public boolean isMaintenance() {
        return this.maintenance;
    }
}

