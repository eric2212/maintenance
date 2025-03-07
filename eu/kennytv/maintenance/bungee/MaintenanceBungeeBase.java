/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.plugin.Plugin
 */
package eu.kennytv.maintenance.bungee;

import eu.kennytv.maintenance.api.Maintenance;
import eu.kennytv.maintenance.bungee.MaintenanceBungeePlugin;
import eu.kennytv.maintenance.core.MaintenanceBase;
import java.io.File;
import net.md_5.bungee.api.plugin.Plugin;

public final class MaintenanceBungeeBase
extends Plugin
implements MaintenanceBase {
    private MaintenanceBungeePlugin maintenance;

    public void onEnable() {
        this.maintenance = new MaintenanceBungeePlugin(this);
    }

    public void onDisable() {
        this.maintenance.disable();
    }

    @Override
    public Maintenance getApi() {
        return this.maintenance;
    }

    File getPluginFile() {
        return this.getFile();
    }
}

