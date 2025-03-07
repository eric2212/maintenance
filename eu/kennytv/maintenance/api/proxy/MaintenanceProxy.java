/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.api.proxy;

import eu.kennytv.maintenance.api.Maintenance;
import eu.kennytv.maintenance.api.proxy.Server;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public interface MaintenanceProxy
extends Maintenance {
    public boolean setMaintenanceToServer(Server var1, boolean var2);

    public boolean isMaintenance(Server var1);

    public boolean isServerTaskRunning(Server var1);

    @Nullable
    public Server getServer(String var1);

    public Set<String> getServers();

    public Set<String> getMaintenanceServers();
}

