/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.util;

import eu.kennytv.maintenance.api.MaintenanceProvider;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.MiniMessage;
import java.util.UUID;

public interface SenderInfo {
    public UUID getUuid();

    public String getName();

    public boolean hasPermission(String var1);

    default public boolean hasMaintenancePermission(String permission) {
        return permission == null || this.hasPermission("maintenance.admin") || this.hasPermission("maintenance." + permission);
    }

    public void send(Component var1);

    default public void sendRich(String message) {
        this.send((Component)MiniMessage.miniMessage().deserialize(message));
    }

    default public void sendPrefixedRich(String message) {
        MaintenancePlugin maintenance = (MaintenancePlugin)MaintenanceProvider.get();
        this.send(maintenance.prefix().append((Component)MiniMessage.miniMessage().deserialize(message)));
    }

    public boolean isPlayer();
}

