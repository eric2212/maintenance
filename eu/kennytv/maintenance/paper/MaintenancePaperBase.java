/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package eu.kennytv.maintenance.paper;

import eu.kennytv.maintenance.api.Maintenance;
import eu.kennytv.maintenance.core.MaintenanceBase;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.paper.MaintenancePaperPlugin;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public final class MaintenancePaperBase
extends JavaPlugin
implements MaintenanceBase {
    private MaintenancePlugin maintenance;

    public void onEnable() {
        this.maintenance = new MaintenancePaperPlugin(this);
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

