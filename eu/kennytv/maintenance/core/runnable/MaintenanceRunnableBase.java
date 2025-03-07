/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.runnable;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.util.Task;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;

public abstract class MaintenanceRunnableBase
implements Runnable {
    protected final MaintenancePlugin plugin;
    protected final Settings settings;
    protected final boolean enable;
    private final Task task;
    protected int seconds;

    protected MaintenanceRunnableBase(MaintenancePlugin plugin, Settings settings, int seconds, boolean enable) {
        this.plugin = plugin;
        this.settings = settings;
        this.seconds = seconds;
        this.enable = enable;
        this.task = plugin.startMaintenanceRunnable(this);
    }

    @Override
    public void run() {
        if (this.seconds == 0) {
            this.finish();
        } else if (this.settings.getBroadcastIntervals().contains(this.seconds)) {
            this.broadcast(this.enable ? this.getStartMessage() : this.getEndMessage());
        }
        --this.seconds;
    }

    public String getTime() {
        return this.plugin.getFormattedTime(this.seconds);
    }

    public boolean shouldEnable() {
        return this.enable;
    }

    public int getSecondsLeft() {
        return this.seconds;
    }

    public Task getTask() {
        return this.task;
    }

    protected void broadcast(Component message) {
        this.plugin.broadcast(message);
    }

    protected abstract void finish();

    protected abstract Component getStartMessage();

    protected abstract Component getEndMessage();
}

