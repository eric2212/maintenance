/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.runnable;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.runnable.SingleMaintenanceRunnable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.time.Duration;

public final class SingleMaintenanceScheduleRunnable
extends SingleMaintenanceRunnable {
    private final int maintenanceDurationSeconds;

    public SingleMaintenanceScheduleRunnable(MaintenancePlugin plugin, Settings settings, int seconds, int maintenanceDuration, Server server) {
        super(plugin, settings, seconds, true, server);
        this.maintenanceDurationSeconds = maintenanceDuration;
    }

    @Override
    protected void finish() {
        super.finish();
        ((MaintenanceProxyPlugin)this.plugin).startSingleMaintenanceRunnable(this.server, Duration.ofSeconds(this.maintenanceDurationSeconds), false);
    }

    @Override
    protected Component getStartMessage() {
        return this.settings.getMessage("singleScheduletimerBroadcast", "%SERVER%", this.server.getName(), "%TIME%", this.getTime(), "%DURATION%", this.plugin.getFormattedTime(this.maintenanceDurationSeconds));
    }
}

