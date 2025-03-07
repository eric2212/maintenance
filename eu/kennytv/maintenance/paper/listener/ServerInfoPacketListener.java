/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Status$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.wrappers.WrappedGameProfile
 *  com.comphenix.protocol.wrappers.WrappedServerPing
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.ServerListPingEvent
 *  org.bukkit.plugin.Plugin
 */
package eu.kennytv.maintenance.paper.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.paper.MaintenancePaperBase;
import eu.kennytv.maintenance.paper.MaintenancePaperPlugin;
import eu.kennytv.maintenance.paper.util.ComponentUtil;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

public final class ServerInfoPacketListener
extends PacketAdapter
implements Listener {
    private static final UUID ZERO_UUID = new UUID(0L, 0L);
    private final MaintenancePaperPlugin plugin;
    private final Settings settings;

    public ServerInfoPacketListener(MaintenancePaperPlugin plugin, MaintenancePaperBase base, Settings settings) {
        super((Plugin)base, ListenerPriority.HIGHEST, new PacketType[]{PacketType.Status.Server.SERVER_INFO});
        this.plugin = plugin;
        this.settings = settings;
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)this);
    }

    public void onPacketSending(PacketEvent event) {
        if (!this.settings.isMaintenance()) {
            return;
        }
        WrappedServerPing ping = (WrappedServerPing)event.getPacket().getServerPings().read(0);
        if (this.settings.isEnablePingMessages()) {
            ping.setMotD(ComponentUtil.toLegacy(this.settings.getRandomPingMessage()));
        }
        if (this.settings.hasCustomPlayerCountMessage()) {
            ping.setVersionProtocol(1);
            ping.setVersionName(this.settings.getLegacyParsedPlayerCountMessage());
        }
        if (this.settings.hasCustomPlayerCountHoverMessage()) {
            ArrayList<WrappedGameProfile> players = new ArrayList<WrappedGameProfile>();
            for (String string : this.settings.getLegacyParsedPlayerCountHoverLines()) {
                players.add(new WrappedGameProfile(ZERO_UUID, string));
            }
            ping.setPlayers(players);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void serverListPing(ServerListPingEvent event) {
        if (this.settings.isMaintenance() && this.settings.hasCustomIcon() && this.plugin.getFavicon() != null) {
            try {
                event.setServerIcon(this.plugin.getFavicon());
            } catch (UnsupportedOperationException unsupportedOperationException) {
                // empty catch block
            }
        }
    }
}

