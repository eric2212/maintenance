/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core;

import eu.kennytv.maintenance.api.Maintenance;

@FunctionalInterface
public interface MaintenanceBase {
    public Maintenance getApi();
}

