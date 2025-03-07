/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  net.md_5.bungee.api.CommandSender
 *  net.md_5.bungee.api.ProxyServer
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 *  net.md_5.bungee.api.event.PlayerDisconnectEvent
 *  net.md_5.bungee.api.event.PostLoginEvent
 *  net.md_5.bungee.api.plugin.Listener
 *  net.md_5.bungee.api.plugin.Plugin
 *  net.md_5.bungee.event.EventHandler
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord;

import com.google.gson.Gson;
import eu.kennytv.maintenance.lib.kyori.adventure.audience.Audience;
import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identity;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord.BungeeAudience;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord.BungeeAudiences;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord.BungeeFacet;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.FacetAudienceProvider;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.Knob;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointered;
import eu.kennytv.maintenance.lib.kyori.adventure.text.flattener.ComponentFlattener;
import eu.kennytv.maintenance.lib.kyori.adventure.text.renderer.ComponentRenderer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.translation.GlobalTranslator;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

final class BungeeAudiencesImpl
extends FacetAudienceProvider<CommandSender, BungeeAudience>
implements BungeeAudiences {
    private static final Map<String, BungeeAudiences> INSTANCES;
    private final Plugin plugin;
    private final Listener listener;

    @NotNull
    static BungeeAudiences instanceFor(@NotNull Plugin plugin) {
        return BungeeAudiencesImpl.builder(plugin).build();
    }

    @NotNull
    static Builder builder(@NotNull Plugin plugin) {
        return new Builder(plugin);
    }

    BungeeAudiencesImpl(Plugin plugin, @NotNull ComponentRenderer<Pointered> componentRenderer) {
        super(componentRenderer);
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.listener = new Listener();
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, (net.md_5.bungee.api.plugin.Listener)this.listener);
        CommandSender console = this.plugin.getProxy().getConsole();
        this.addViewer(console);
        for (ProxiedPlayer player : this.plugin.getProxy().getPlayers()) {
            this.addViewer(player);
        }
    }

    @Override
    @NotNull
    public Audience sender(@NotNull CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return this.player((ProxiedPlayer)sender);
        }
        if (ProxyServer.getInstance().getConsole().equals(sender)) {
            return this.console();
        }
        return this.createAudience(Collections.singletonList(sender));
    }

    @Override
    @NotNull
    public Audience player(@NotNull ProxiedPlayer player) {
        return this.player(player.getUniqueId());
    }

    @Override
    @NotNull
    protected BungeeAudience createAudience(@NotNull Collection<CommandSender> viewers) {
        return new BungeeAudience(this, viewers);
    }

    @Override
    @NotNull
    public ComponentFlattener flattener() {
        return BungeeFacet.FLATTENER;
    }

    @Override
    public void close() {
        INSTANCES.remove(this.plugin.getDescription().getName());
        this.plugin.getProxy().getPluginManager().unregisterListener((net.md_5.bungee.api.plugin.Listener)this.listener);
        super.close();
    }

    static {
        Knob.OUT = message -> ProxyServer.getInstance().getLogger().log(Level.INFO, (String)message);
        Knob.ERR = (message, error) -> ProxyServer.getInstance().getLogger().log(Level.WARNING, (String)message, (Throwable)error);
        try {
            Field gsonField = ProxyServer.getInstance().getClass().getDeclaredField("gson");
            gsonField.setAccessible(true);
            Gson gson = (Gson)gsonField.get(ProxyServer.getInstance());
            BungeeComponentSerializer.inject(gson);
        } catch (Throwable error2) {
            Knob.logError(error2, "Failed to inject ProxyServer gson", new Object[0]);
        }
        INSTANCES = Collections.synchronizedMap(new HashMap(4));
    }

    public final class Listener
    implements net.md_5.bungee.api.plugin.Listener {
        @EventHandler(priority=-128)
        public void onLogin(PostLoginEvent event) {
            BungeeAudiencesImpl.this.addViewer(event.getPlayer());
        }

        @EventHandler(priority=127)
        public void onDisconnect(PlayerDisconnectEvent event) {
            BungeeAudiencesImpl.this.removeViewer(event.getPlayer());
        }
    }

    static final class Builder
    implements BungeeAudiences.Builder {
        @NotNull
        private final Plugin plugin;
        private ComponentRenderer<Pointered> componentRenderer;

        Builder(@NotNull Plugin plugin) {
            this.plugin = Objects.requireNonNull(plugin, "plugin");
            this.componentRenderer(ptr -> ptr.getOrDefault(Identity.LOCALE, DEFAULT_LOCALE), GlobalTranslator.renderer());
        }

        @Override
        @NotNull
        public Builder componentRenderer(@NotNull ComponentRenderer<Pointered> componentRenderer) {
            this.componentRenderer = Objects.requireNonNull(componentRenderer, "component renderer");
            return this;
        }

        @Override
        public @NotNull BungeeAudiences.Builder partition(@NotNull Function<Pointered, ?> partitionFunction) {
            Objects.requireNonNull(partitionFunction, "partitionFunction");
            return this;
        }

        @Override
        @NotNull
        public BungeeAudiences build() {
            return INSTANCES.computeIfAbsent(this.plugin.getDescription().getName(), name -> new BungeeAudiencesImpl(this.plugin, this.componentRenderer));
        }
    }
}

