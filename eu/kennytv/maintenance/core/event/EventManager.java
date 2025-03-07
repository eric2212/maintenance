/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.event;

import eu.kennytv.maintenance.api.event.manager.EventListener;
import eu.kennytv.maintenance.api.event.manager.MaintenanceEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventManager
implements eu.kennytv.maintenance.api.event.manager.EventManager {
    private final Map<String, List<EventListener>> listeners = new HashMap<String, List<EventListener>>();

    @Override
    public void registerListener(EventListener listener, Class<? extends MaintenanceEvent> eventClass) {
        this.listeners.computeIfAbsent(eventClass.getSimpleName(), s -> new ArrayList()).add(listener);
    }

    @Override
    public void callEvent(MaintenanceEvent event) {
        List<EventListener> list = this.listeners.get(event.getClass().getSimpleName());
        if (list != null) {
            for (EventListener listener : list) {
                listener.onEvent(event);
            }
        }
    }
}

