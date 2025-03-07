/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.CommandSender
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord;

import eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord.BungeeAudiencesImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord.BungeeFacet;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.Facet;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.facet.FacetAudience;
import java.util.Collection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

final class BungeeAudience
extends FacetAudience<CommandSender> {
    private static final Collection<Facet.Chat<? extends CommandSender, ?>> CHAT = Facet.of(BungeeFacet.ChatPlayerSenderId::new, BungeeFacet.ChatPlayer::new, BungeeFacet.ChatConsole::new);
    private static final Collection<Facet.ActionBar<ProxiedPlayer, ?>> ACTION_BAR = Facet.of(BungeeFacet.ActionBar::new);
    private static final Collection<Facet.Title<ProxiedPlayer, ?, ?, ?>> TITLE = Facet.of(BungeeFacet.Title::new);
    private static final Collection<Facet.BossBar.Builder<ProxiedPlayer, ? extends Facet.BossBar<ProxiedPlayer>>> BOSS_BAR = Facet.of(BungeeFacet.BossBar.Builder::new);
    private static final Collection<Facet.TabList<ProxiedPlayer, ?>> TAB_LIST = Facet.of(BungeeFacet.TabList::new);
    private static final Collection<Facet.Pointers<? extends CommandSender>> POINTERS = Facet.of(BungeeFacet.CommandSenderPointers::new, BungeeFacet.PlayerPointers::new);

    BungeeAudience(@NotNull BungeeAudiencesImpl provider, @NotNull Collection<? extends CommandSender> viewers) {
        super(provider, viewers, CHAT, ACTION_BAR, TITLE, null, null, null, BOSS_BAR, TAB_LIST, POINTERS);
    }
}

