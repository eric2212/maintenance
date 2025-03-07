/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.event.server.PaperServerListPingEvent
 *  com.destroystokyo.paper.profile.PlayerProfile
 *  com.destroystokyo.paper.profile.ProfileProperty
 *  com.mojang.authlib.GameProfile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package eu.kennytv.maintenance.paper.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.paper.MaintenancePaperPlugin;
import eu.kennytv.maintenance.paper.util.ComponentUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class PaperServerListPingListener
implements Listener {
    private final MaintenancePaperPlugin plugin;
    private final Settings settings;

    public PaperServerListPingListener(MaintenancePaperPlugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void serverListPing(PaperServerListPingEvent event) {
        if (!this.settings.isMaintenance()) {
            return;
        }
        if (this.settings.isEnablePingMessages()) {
            if (ComponentUtil.PAPER) {
                event.motd(ComponentUtil.toPaperComponent(this.settings.getRandomPingMessage()));
            } else {
                event.setMotd(ComponentUtil.toLegacy(this.settings.getRandomPingMessage()));
            }
        }
        if (this.settings.hasCustomPlayerCountMessage()) {
            event.setProtocolVersion(-1);
            event.setVersion(this.settings.getLegacyParsedPlayerCountMessage());
        }
        if (this.settings.hasCustomPlayerCountHoverMessage()) {
            List sample = event.getPlayerSample();
            sample.clear();
            for (String string : this.settings.getLegacyParsedPlayerCountHoverLines()) {
                sample.add(new DummyProfile(string));
            }
        }
        if (this.settings.hasCustomIcon() && this.plugin.getFavicon() != null) {
            event.setServerIcon(this.plugin.getFavicon());
        }
    }

    private static final class DummyProfile
    implements PlayerProfile {
        private final String name;

        private DummyProfile(String name) {
            this.name = name;
        }

        @Nullable
        public String getName() {
            return this.name;
        }

        public String setName(@Nullable String name) {
            return this.name;
        }

        public UUID getId() {
            return UUID.randomUUID();
        }

        public UUID setId(@Nullable UUID uuid) {
            return null;
        }

        @Nonnull
        public Set<ProfileProperty> getProperties() {
            return Collections.emptySet();
        }

        public boolean hasProperty(String property) {
            return false;
        }

        public void setProperty(ProfileProperty property) {
        }

        public void setProperties(Collection<ProfileProperty> properties) {
        }

        public boolean removeProperty(String property) {
            return false;
        }

        public void clearProperties() {
        }

        public boolean isComplete() {
            return false;
        }

        public boolean completeFromCache() {
            return false;
        }

        public boolean completeFromCache(boolean b) {
            return false;
        }

        public boolean completeFromCache(boolean b, boolean b1) {
            return false;
        }

        public boolean complete(boolean textures) {
            return false;
        }

        public boolean complete(boolean b, boolean b1) {
            return false;
        }

        public GameProfile getGameProfile() {
            return null;
        }
    }
}

