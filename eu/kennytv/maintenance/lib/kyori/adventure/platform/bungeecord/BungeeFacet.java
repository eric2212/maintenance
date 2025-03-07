/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.CommandSender
 *  net.md_5.bungee.api.ProxyServer
 *  net.md_5.bungee.api.Title
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.md_5.bungee.api.connection.Connection
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 *  net.md_5.bungee.chat.ComponentSerializer
 *  net.md_5.bungee.chat.TranslationRegistry
 *  net.md_5.bungee.protocol.DefinedPacket
 *  net.md_5.bungee.protocol.packet.BossBar
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord;

import eu.kennytv.maintenance.lib.kyori.adventure.audience.MessageType;
import eu.kennytv.maintenance.lib.kyori.adventure.bossbar.BossBar;
import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identity;
import eu.kennytv.maintenance.lib.kyori.adventure.permission.PermissionChecker;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord.BungeeReflection;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.Facet;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.FacetBase;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.FacetComponentFlattener;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.FacetPointers;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.Knob;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointers;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.flattener.ComponentFlattener;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.util.TriState;
import java.lang.invoke.MethodHandle;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.chat.TranslationRegistry;
import net.md_5.bungee.protocol.DefinedPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class BungeeFacet<V extends CommandSender>
extends FacetBase<V> {
    static final BaseComponent[] EMPTY_COMPONENT_ARRAY = new BaseComponent[0];
    private static final Collection<? extends FacetComponentFlattener.Translator<ProxyServer>> TRANSLATORS = Facet.of(Translator::new);
    static final ComponentFlattener FLATTENER = FacetComponentFlattener.get(ProxyServer.getInstance(), TRANSLATORS);
    static final BungeeComponentSerializer MODERN = BungeeComponentSerializer.of(GsonComponentSerializer.gson(), LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().flattener(FLATTENER).build());
    static final BungeeComponentSerializer LEGACY = BungeeComponentSerializer.of(GsonComponentSerializer.builder().downsampleColors().emitLegacyHoverEvent().build(), LegacyComponentSerializer.builder().flattener(FLATTENER).build());

    protected BungeeFacet(@Nullable Class<? extends V> viewerClass) {
        super(viewerClass);
    }

    static class Translator
    extends FacetBase<ProxyServer>
    implements FacetComponentFlattener.Translator<ProxyServer> {
        private static final boolean SUPPORTED;

        Translator() {
            super(ProxyServer.class);
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && SUPPORTED;
        }

        @Override
        @NotNull
        public String valueOrDefault(@NotNull ProxyServer game, @NotNull String key) {
            return TranslationRegistry.INSTANCE.translate(key);
        }

        static {
            boolean supported;
            try {
                Class.forName("net.md_5.bungee.chat.TranslationRegistry");
                supported = true;
            } catch (ClassNotFoundException ex) {
                supported = false;
            }
            SUPPORTED = supported;
        }
    }

    static final class PlayerPointers
    extends BungeeFacet<ProxiedPlayer>
    implements Facet.Pointers<ProxiedPlayer> {
        PlayerPointers() {
            super(ProxiedPlayer.class);
        }

        @Override
        public void contributePointers(ProxiedPlayer viewer, Pointers.Builder builder) {
            builder.withDynamic(Identity.UUID, () -> ((ProxiedPlayer)viewer).getUniqueId());
            builder.withDynamic(Identity.LOCALE, () -> ((ProxiedPlayer)viewer).getLocale());
            builder.withDynamic(FacetPointers.SERVER, () -> viewer.getServer().getInfo().getName());
            builder.withStatic(FacetPointers.TYPE, FacetPointers.Type.PLAYER);
        }
    }

    static final class CommandSenderPointers
    extends BungeeFacet<CommandSender>
    implements Facet.Pointers<CommandSender> {
        CommandSenderPointers() {
            super(CommandSender.class);
        }

        @Override
        public void contributePointers(CommandSender viewer, Pointers.Builder builder) {
            builder.withDynamic(Identity.NAME, () -> ((CommandSender)viewer).getName());
            builder.withStatic(PermissionChecker.POINTER, perm -> viewer.hasPermission(perm) ? TriState.TRUE : TriState.FALSE);
            if (!(viewer instanceof ProxiedPlayer)) {
                builder.withStatic(FacetPointers.TYPE, viewer == ProxyServer.getInstance().getConsole() ? FacetPointers.Type.CONSOLE : FacetPointers.Type.OTHER);
            }
        }
    }

    static final class TabList
    extends Message
    implements Facet.TabList<ProxiedPlayer, BaseComponent[]> {
        TabList() {
        }

        @Override
        public void send(ProxiedPlayer viewer, BaseComponent @Nullable [] header, BaseComponent @Nullable [] footer) {
            viewer.setTabHeader(header == null ? EMPTY_COMPONENT_ARRAY : header, footer == null ? EMPTY_COMPONENT_ARRAY : footer);
        }
    }

    static class BossBar
    extends Message
    implements Facet.BossBarPacket<ProxiedPlayer> {
        private static MethodHandle SET_TITLE_STRING;
        private static MethodHandle SET_TITLE_COMPONENT;
        private final Set<ProxiedPlayer> viewers;
        private final net.md_5.bungee.protocol.packet.BossBar bar;
        private volatile boolean initialized = false;

        protected BossBar(@NotNull Collection<ProxiedPlayer> viewers) {
            this.viewers = new CopyOnWriteArraySet<ProxiedPlayer>(viewers);
            this.bar = new net.md_5.bungee.protocol.packet.BossBar(UUID.randomUUID(), 0);
        }

        @Override
        public void bossBarInitialized(@NotNull eu.kennytv.maintenance.lib.kyori.adventure.bossbar.BossBar bar) {
            Facet.BossBarPacket.super.bossBarInitialized(bar);
            this.initialized = true;
            this.broadcastPacket(0);
        }

        @Override
        public void bossBarNameChanged(@NotNull eu.kennytv.maintenance.lib.kyori.adventure.bossbar.BossBar bar, @NotNull Component oldName, @NotNull Component newName) {
            if (!this.viewers.isEmpty()) {
                BaseComponent[] message = this.createMessage(this.viewers.iterator().next(), newName);
                this.updateBarTitle(message);
                this.broadcastPacket(3);
            }
        }

        @Override
        public void bossBarProgressChanged(@NotNull eu.kennytv.maintenance.lib.kyori.adventure.bossbar.BossBar bar, float oldPercent, float newPercent) {
            this.bar.setHealth(newPercent);
            this.broadcastPacket(2);
        }

        @Override
        public void bossBarColorChanged(@NotNull eu.kennytv.maintenance.lib.kyori.adventure.bossbar.BossBar bar, @NotNull BossBar.Color oldColor, @NotNull BossBar.Color newColor) {
            this.bar.setColor(this.createColor(newColor));
            this.broadcastPacket(4);
        }

        @Override
        public void bossBarOverlayChanged(@NotNull eu.kennytv.maintenance.lib.kyori.adventure.bossbar.BossBar bar, @NotNull BossBar.Overlay oldOverlay, @NotNull BossBar.Overlay newOverlay) {
            this.bar.setDivision(this.createOverlay(newOverlay));
            this.broadcastPacket(4);
        }

        @Override
        public void bossBarFlagsChanged(@NotNull eu.kennytv.maintenance.lib.kyori.adventure.bossbar.BossBar bar, @NotNull Set<BossBar.Flag> flagsAdded, @NotNull Set<BossBar.Flag> flagsRemoved) {
            this.bar.setFlags(this.createFlag(this.bar.getFlags(), flagsAdded, flagsRemoved));
            this.broadcastPacket(5);
        }

        @Override
        public void addViewer(@NotNull ProxiedPlayer viewer) {
            this.viewers.add(viewer);
            this.sendPacket(0, viewer);
        }

        @Override
        public void removeViewer(@NotNull ProxiedPlayer viewer) {
            this.viewers.remove(viewer);
            this.sendPacket(1, viewer);
        }

        @Override
        public boolean isEmpty() {
            return !this.initialized || this.viewers.isEmpty();
        }

        @Override
        public void close() {
            this.broadcastPacket(1);
            this.viewers.clear();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void broadcastPacket(int action) {
            if (this.isEmpty()) {
                return;
            }
            net.md_5.bungee.protocol.packet.BossBar bossBar = this.bar;
            synchronized (bossBar) {
                this.bar.setAction(action);
                for (ProxiedPlayer viewer : this.viewers) {
                    viewer.unsafe().sendPacket((DefinedPacket)this.bar);
                }
            }
        }

        private void updateBarTitle(BaseComponent[] message) {
            try {
                if (SET_TITLE_STRING != null) {
                    SET_TITLE_STRING.invoke(this.bar, ComponentSerializer.toString((BaseComponent[])message));
                } else {
                    SET_TITLE_COMPONENT.invoke(this.bar, TextComponent.fromArray((BaseComponent[])message));
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Cannot update the BossBar title", new Object[0]);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void sendPacket(int action, ProxiedPlayer ... viewers) {
            net.md_5.bungee.protocol.packet.BossBar bossBar = this.bar;
            synchronized (bossBar) {
                int lastAction = this.bar.getAction();
                this.bar.setAction(action);
                for (ProxiedPlayer viewer : viewers) {
                    viewer.unsafe().sendPacket((DefinedPacket)this.bar);
                }
                this.bar.setAction(lastAction);
            }
        }

        static {
            Class<net.md_5.bungee.protocol.packet.BossBar> bossBarClass = net.md_5.bungee.protocol.packet.BossBar.class;
            if (BungeeReflection.hasMethod(bossBarClass, "setTitle", String.class)) {
                SET_TITLE_STRING = BungeeReflection.findMethod(bossBarClass, "setTitle", Void.TYPE, String.class);
            } else {
                SET_TITLE_COMPONENT = BungeeReflection.findMethod(bossBarClass, "setTitle", Void.TYPE, BaseComponent.class);
            }
        }

        static class Builder
        extends BungeeFacet<ProxiedPlayer>
        implements Facet.BossBar.Builder<ProxiedPlayer, BossBar> {
            protected Builder() {
                super(ProxiedPlayer.class);
            }

            @Override
            public boolean isApplicable(@NotNull ProxiedPlayer viewer) {
                return super.isApplicable(viewer) && viewer.getPendingConnection().getVersion() >= 356;
            }

            @Override
            public @NotNull BossBar createBossBar(@NotNull Collection<ProxiedPlayer> viewers) {
                return new BossBar(viewers);
            }
        }
    }

    static class Title
    extends Message
    implements Facet.Title<ProxiedPlayer, BaseComponent[], net.md_5.bungee.api.Title, net.md_5.bungee.api.Title> {
        private static final net.md_5.bungee.api.Title CLEAR = ProxyServer.getInstance().createTitle().clear();
        private static final net.md_5.bungee.api.Title RESET = ProxyServer.getInstance().createTitle().reset();

        Title() {
        }

        @Override
        public @NotNull net.md_5.bungee.api.Title createTitleCollection() {
            return ProxyServer.getInstance().createTitle();
        }

        @Override
        public void contributeTitle(@NotNull net.md_5.bungee.api.Title coll, BaseComponent @NotNull [] title) {
            coll.title(title);
        }

        @Override
        public void contributeSubtitle(@NotNull net.md_5.bungee.api.Title coll, BaseComponent @NotNull [] subtitle) {
            coll.subTitle(subtitle);
        }

        @Override
        public void contributeTimes(@NotNull net.md_5.bungee.api.Title coll, int inTicks, int stayTicks, int outTicks) {
            if (inTicks > -1) {
                coll.fadeIn(inTicks);
            }
            if (stayTicks > -1) {
                coll.stay(stayTicks);
            }
            if (outTicks > -1) {
                coll.fadeOut(outTicks);
            }
        }

        @Override
        @Nullable
        public net.md_5.bungee.api.Title completeTitle(@NotNull net.md_5.bungee.api.Title coll) {
            return coll;
        }

        @Override
        public void showTitle(@NotNull ProxiedPlayer viewer, @NotNull net.md_5.bungee.api.Title title) {
            viewer.sendTitle(title);
        }

        @Override
        public void clearTitle(@NotNull ProxiedPlayer viewer) {
            viewer.sendTitle(CLEAR);
        }

        @Override
        public void resetTitle(@NotNull ProxiedPlayer viewer) {
            viewer.sendTitle(RESET);
        }
    }

    static class ActionBar
    extends Message
    implements Facet.ActionBar<ProxiedPlayer, BaseComponent[]> {
        ActionBar() {
        }

        @Override
        public void sendMessage(@NotNull ProxiedPlayer viewer, BaseComponent @NotNull [] message) {
            viewer.sendMessage(ChatMessageType.ACTION_BAR, message);
        }
    }

    static class ChatPlayer
    extends Message
    implements Facet.Chat<ProxiedPlayer, BaseComponent[]> {
        ChatPlayer() {
        }

        @Nullable
        public ChatMessageType createType(@NotNull MessageType type) {
            if (type == MessageType.CHAT) {
                return ChatMessageType.CHAT;
            }
            if (type == MessageType.SYSTEM) {
                return ChatMessageType.SYSTEM;
            }
            Knob.logUnsupported(this, (Object)type);
            return null;
        }

        @Override
        public void sendMessage(@NotNull ProxiedPlayer viewer, @NotNull Identity source, BaseComponent @NotNull [] message, @NotNull Object type) {
            ChatMessageType chat;
            ChatMessageType chatMessageType = chat = type instanceof MessageType ? this.createType((MessageType)((Object)type)) : ChatMessageType.SYSTEM;
            if (chat != null) {
                viewer.sendMessage(chat, message);
            }
        }
    }

    static class ChatPlayerSenderId
    extends ChatPlayer
    implements Facet.Chat<ProxiedPlayer, BaseComponent[]> {
        private static final boolean SUPPORTED;

        ChatPlayerSenderId() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && SUPPORTED;
        }

        @Override
        public void sendMessage(@NotNull ProxiedPlayer viewer, @NotNull Identity source, BaseComponent @NotNull [] message, @NotNull Object type) {
            if (type == MessageType.CHAT) {
                viewer.sendMessage(source.uuid(), message);
            } else {
                super.sendMessage(viewer, source, message, type);
            }
        }

        static {
            boolean supported;
            try {
                ProxiedPlayer.class.getMethod("sendMessage", UUID.class, BaseComponent.class);
                supported = true;
            } catch (NoSuchMethodException ex) {
                supported = false;
            }
            SUPPORTED = supported;
        }
    }

    static class Message
    extends BungeeFacet<ProxiedPlayer>
    implements Facet.Message<ProxiedPlayer, BaseComponent[]> {
        protected Message() {
            super(ProxiedPlayer.class);
        }

        @Override
        public BaseComponent @NotNull [] createMessage(@NotNull ProxiedPlayer viewer, @NotNull Component message) {
            if (viewer.getPendingConnection().getVersion() >= 713) {
                return MODERN.serialize(message);
            }
            return LEGACY.serialize(message);
        }
    }

    static class ChatConsole
    extends BungeeFacet<CommandSender>
    implements Facet.Chat<CommandSender, BaseComponent[]> {
        protected ChatConsole() {
            super(CommandSender.class);
        }

        @Override
        public boolean isApplicable(@NotNull CommandSender viewer) {
            return super.isApplicable(viewer) && !(viewer instanceof Connection);
        }

        @Override
        public BaseComponent @NotNull [] createMessage(@NotNull CommandSender viewer, @NotNull Component message) {
            return LEGACY.serialize(message);
        }

        @Override
        public void sendMessage(@NotNull CommandSender viewer, @NotNull Identity source, BaseComponent @NotNull [] message, @NotNull Object type) {
            viewer.sendMessage(message);
        }
    }
}

