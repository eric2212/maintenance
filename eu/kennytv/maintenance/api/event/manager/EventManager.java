/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.api.event.manager;

import eu.kennytv.maintenance.api.event.manager.EventListener;
import eu.kennytv.maintenance.api.event.manager.MaintenanceEvent;

public interface EventManager {
    public void registerListener(EventListener var1, Class<? extends MaintenanceEvent> var2);

    public void callEvent(MaintenanceEvent var1);
}

