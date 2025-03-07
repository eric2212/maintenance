/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.api.event.manager;

import eu.kennytv.maintenance.api.event.manager.MaintenanceEvent;

public abstract class EventListener<T extends MaintenanceEvent> {
    public abstract void onEvent(T var1);
}

