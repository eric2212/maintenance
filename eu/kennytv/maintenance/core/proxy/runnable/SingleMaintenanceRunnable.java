/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.runnable;

import eu.kennytv.maintenance.api.proxy.MaintenanceProxy;
import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.runnable.MaintenanceRunnableBase;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;

public class SingleMaintenanceRunnable
extends MaintenanceRunnableBase {
    protected final Server server;

    public SingleMaintenanceRunnable(MaintenancePlugin plugin, Settings settings, int seconds, boolean enable, Server server) {
        super(plugin, settings, seconds, enable);
        this.server = server;
    }

    @Override
    protected void broadcast(Component component) {
        this.server.broadcast(component);
    }

    @Override
    protected void finish() {
        ((MaintenanceProxy)((Object)this.plugin)).setMaintenanceToServer(this.server, this.enable);
    }

    @Override
    protected Component getStartMessage() {
        return this.settings.getMessage("singleStarttimerBroadcast", "%TIME%", this.getTime(), "%SERVER%", this.server.getName());
    }

    @Override
    protected Component getEndMessage() {
        return this.settings.getMessage("singleEndtimerBroadcast", "%TIME%", this.getTime(), "%SERVER%", this.server.getName());
    }
}

