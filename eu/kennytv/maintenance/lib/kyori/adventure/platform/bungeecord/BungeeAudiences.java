/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.CommandSender
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 *  net.md_5.bungee.api.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord;

import eu.kennytv.maintenance.lib.kyori.adventure.audience.Audience;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.AudienceProvider;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord.BungeeAudiencesImpl;
import java.util.function.Predicate;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface BungeeAudiences
extends AudienceProvider {
    @NotNull
    public static BungeeAudiences create(@NotNull Plugin plugin) {
        return BungeeAudiencesImpl.instanceFor(plugin);
    }

    @NotNull
    public static Builder builder(@NotNull Plugin plugin) {
        return BungeeAudiencesImpl.builder(plugin);
    }

    @NotNull
    public Audience sender(@NotNull CommandSender var1);

    @NotNull
    public Audience player(@NotNull ProxiedPlayer var1);

    @NotNull
    public Audience filter(@NotNull Predicate<CommandSender> var1);

    public static interface Builder
    extends AudienceProvider.Builder<BungeeAudiences, Builder> {
    }
}

