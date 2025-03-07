/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.api;

import eu.kennytv.maintenance.api.Settings;
import eu.kennytv.maintenance.api.event.manager.EventManager;

public interface Maintenance {
    public void setMaintenance(boolean var1);

    public boolean isMaintenance();

    public boolean isTaskRunning();

    public String getVersion();

    public Settings getSettings();

    public EventManager getEventManager();

    public boolean isDebug();

    public void setDebug(boolean var1);
}

