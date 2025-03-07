/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.api;

import eu.kennytv.maintenance.api.Maintenance;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class MaintenanceProvider {
    private static Maintenance maintenance;

    private MaintenanceProvider() {
        throw new IllegalArgumentException();
    }

    @ApiStatus.Internal
    public static void setMaintenance(Maintenance maintenance) {
        if (MaintenanceProvider.maintenance != null) {
            throw new IllegalArgumentException("MaintenanceProvider is already set!");
        }
        MaintenanceProvider.maintenance = maintenance;
    }

    @Nullable
    public static Maintenance get() {
        return maintenance;
    }
}

