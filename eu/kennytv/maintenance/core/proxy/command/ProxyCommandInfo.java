/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.command;

import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;

public abstract class ProxyCommandInfo
extends CommandInfo {
    protected final MaintenanceProxyPlugin plugin;

    protected ProxyCommandInfo(MaintenanceProxyPlugin plugin, String permission) {
        super(plugin, permission);
        this.plugin = plugin;
    }

    @Override
    protected SettingsProxy getSettings() {
        return this.plugin.getSettingsProxy();
    }
}

