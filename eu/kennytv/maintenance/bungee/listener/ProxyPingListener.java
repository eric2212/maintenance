/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ServerPing
 *  net.md_5.bungee.api.ServerPing$PlayerInfo
 *  net.md_5.bungee.api.ServerPing$Players
 *  net.md_5.bungee.api.ServerPing$Protocol
 *  net.md_5.bungee.api.event.ProxyPingEvent
 *  net.md_5.bungee.api.plugin.Listener
 *  net.md_5.bungee.event.EventHandler
 */
package eu.kennytv.maintenance.bungee.listener;

import eu.kennytv.maintenance.bungee.MaintenanceBungeePlugin;
import eu.kennytv.maintenance.bungee.util.ComponentUtil;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class ProxyPingListener
implements Listener {
    private final MaintenanceBungeePlugin plugin;
    private final SettingsProxy settings;

    public ProxyPingListener(MaintenanceBungeePlugin plugin, SettingsProxy settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler(priority=80)
    public void proxyPing(ProxyPingEvent event) {
        if (!this.settings.isMaintenance()) {
            return;
        }
        ServerPing ping = event.getResponse();
        if (this.settings.hasCustomPlayerCountMessage()) {
            ping.setVersion(new ServerPing.Protocol(this.settings.getLegacyParsedPlayerCountMessage(), 1));
        }
        if (this.settings.isEnablePingMessages()) {
            ping.setDescriptionComponent(ComponentUtil.toBadComponents(this.settings.getRandomPingMessage()));
        }
        if (this.settings.hasCustomPlayerCountHoverMessage()) {
            ServerPing.Players players = ping.getPlayers();
            if (players == null) {
                players = new ServerPing.Players(0, 0, null);
                ping.setPlayers(players);
            }
            String[] lines = this.settings.getLegacyParsedPlayerCountHoverLines();
            ServerPing.PlayerInfo[] samplePlayers = new ServerPing.PlayerInfo[lines.length];
            for (int i = 0; i < lines.length; ++i) {
                samplePlayers[i] = new ServerPing.PlayerInfo(lines[i], "");
            }
            players.setSample(samplePlayers);
        }
        if (this.settings.hasCustomIcon() && this.plugin.getFavicon() != null) {
            ping.setFavicon(this.plugin.getFavicon());
        }
    }
}

