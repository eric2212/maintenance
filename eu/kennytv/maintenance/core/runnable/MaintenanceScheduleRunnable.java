/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.runnable;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.runnable.MaintenanceRunnable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.time.Duration;

public final class MaintenanceScheduleRunnable
extends MaintenanceRunnable {
    private final int maintenanceDuration;

    public MaintenanceScheduleRunnable(MaintenancePlugin plugin, Settings settings, int secondsToEnable, int maintenanceDuration) {
        super(plugin, settings, secondsToEnable, true);
        this.maintenanceDuration = maintenanceDuration;
    }

    @Override
    protected void finish() {
        super.finish();
        this.plugin.startMaintenanceRunnable(Duration.ofSeconds(this.maintenanceDuration), false);
    }

    @Override
    protected Component getStartMessage() {
        return this.settings.getMessage("scheduletimerBroadcast", "%TIME%", this.getTime(), "%DURATION%", this.plugin.getFormattedTime(this.maintenanceDuration));
    }
}

