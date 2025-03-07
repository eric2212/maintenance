/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.api.event;

import eu.kennytv.maintenance.api.event.manager.MaintenanceEvent;

public final class MaintenanceChangedEvent
implements MaintenanceEvent {
    private final boolean maintenance;

    public MaintenanceChangedEvent(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public boolean isMaintenance() {
        return this.maintenance;
    }
}

