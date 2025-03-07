/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.ServerListPingEvent
 */
package eu.kennytv.maintenance.paper.listener;

import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.paper.MaintenancePaperPlugin;
import eu.kennytv.maintenance.paper.util.ComponentUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public final class ServerListPingListener
implements Listener {
    private final MaintenancePaperPlugin plugin;
    private final Settings settings;

    public ServerListPingListener(MaintenancePaperPlugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void serverListPing(ServerListPingEvent event) {
        if (!this.settings.isMaintenance()) {
            return;
        }
        if (this.settings.hasCustomPlayerCountMessage()) {
            event.setMaxPlayers(0);
        }
        if (this.settings.isEnablePingMessages()) {
            if (ComponentUtil.PAPER) {
                event.motd(ComponentUtil.toPaperComponent(this.settings.getRandomPingMessage()));
            } else {
                event.setMotd(ComponentUtil.toLegacy(this.settings.getRandomPingMessage()));
            }
        }
        if (this.settings.hasCustomIcon() && this.plugin.getFavicon() != null) {
            try {
                event.setServerIcon(this.plugin.getFavicon());
            } catch (UnsupportedOperationException unsupportedOperationException) {
                // empty catch block
            }
        }
    }
}

