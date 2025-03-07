/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.CommandSender
 *  net.md_5.bungee.api.Favicon
 *  net.md_5.bungee.api.ProxyServer
 *  net.md_5.bungee.api.config.ServerInfo
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 *  net.md_5.bungee.api.connection.Server
 *  net.md_5.bungee.api.plugin.Command
 *  net.md_5.bungee.api.plugin.Listener
 *  net.md_5.bungee.api.plugin.Plugin
 *  net.md_5.bungee.api.plugin.PluginManager
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.bungee;

import eu.kennytv.maintenance.bungee.MaintenanceBungeeBase;
import eu.kennytv.maintenance.bungee.command.MaintenanceBungeeCommand;
import eu.kennytv.maintenance.bungee.command.MaintenanceBungeeCommandBase;
import eu.kennytv.maintenance.bungee.listener.ProxyPingListener;
import eu.kennytv.maintenance.bungee.listener.ServerConnectListener;
import eu.kennytv.maintenance.bungee.util.BungeeSenderInfo;
import eu.kennytv.maintenance.bungee.util.BungeeServer;
import eu.kennytv.maintenance.bungee.util.BungeeTask;
import eu.kennytv.maintenance.bungee.util.ComponentUtil;
import eu.kennytv.maintenance.core.dump.PluginDump;
import eu.kennytv.maintenance.core.hook.ServerListPlusHook;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;
import eu.kennytv.maintenance.core.proxy.util.ProfileLookup;
import eu.kennytv.maintenance.core.proxy.util.ProxyOfflineSenderInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.core.util.ServerType;
import eu.kennytv.maintenance.core.util.Task;
import eu.kennytv.maintenance.lib.bstats.bungeecord.Metrics;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord.BungeeAudiences;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

public final class MaintenanceBungeePlugin
extends MaintenanceProxyPlugin {
    private final MaintenanceBungeeBase plugin;
    private final BungeeAudiences audiences;
    private Favicon favicon;

    MaintenanceBungeePlugin(MaintenanceBungeeBase plugin) {
        super(plugin.getDescription().getVersion(), ServerType.BUNGEE);
        this.plugin = plugin;
        this.audiences = BungeeAudiences.create(plugin);
        this.settingsProxy = new SettingsProxy(this);
        this.settings = this.settingsProxy;
        this.sendEnableMessage();
        PluginManager pm = this.getProxy().getPluginManager();
        pm.registerListener((Plugin)plugin, (Listener)new ProxyPingListener(this, this.settingsProxy));
        pm.registerListener((Plugin)plugin, (Listener)new ServerConnectListener(this, this.settingsProxy));
        this.commandManager = new MaintenanceBungeeCommand(this, this.settingsProxy);
        pm.registerCommand((Plugin)plugin, (Command)new MaintenanceBungeeCommandBase(this.commandManager));
        this.continueLastEndtimer();
        new Metrics(plugin, 742);
        Plugin serverListPlus = pm.getPlugin("ServerListPlus");
        if (serverListPlus != null) {
            this.serverListPlusHook = new ServerListPlusHook(serverListPlus);
            if (this.settings.isEnablePingMessages()) {
                this.serverListPlusHook.setEnabled(!this.settingsProxy.isMaintenance());
            }
            plugin.getLogger().info("Enabled ServerListPlus integration!");
        }
    }

    public boolean isMaintenance(ServerInfo serverInfo) {
        return this.settingsProxy.isMaintenance(serverInfo.getName());
    }

    @Override
    protected void kickPlayersFromProxy() {
        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
            if (this.hasPermission((CommandSender)player, "bypass") || this.settingsProxy.isWhitelisted(player.getUniqueId())) continue;
            player.disconnect(ComponentUtil.toBadComponents(this.settingsProxy.getKickMessage()));
        }
    }

    @Override
    protected void kickPlayers(eu.kennytv.maintenance.api.proxy.Server server, eu.kennytv.maintenance.api.proxy.Server fallback) {
        ServerInfo fallbackServer = fallback != null ? ((BungeeServer)fallback).getServer() : null;
        boolean checkForFallback = fallbackServer != null && !this.isMaintenance(fallback);
        for (ProxiedPlayer player : ((BungeeServer)server).getServer().getPlayers()) {
            if (!this.hasPermission((CommandSender)player, "bypass") && !this.settingsProxy.isWhitelisted(player.getUniqueId())) {
                if (checkForFallback && fallbackServer.canAccess((CommandSender)player)) {
                    this.audiences.player(player).sendMessage(this.settingsProxy.getMessage("singleMaintenanceActivated", "%SERVER%", server.getName()));
                    player.connect(fallbackServer);
                    continue;
                }
                player.disconnect(ComponentUtil.toBadComponents(this.settingsProxy.getFullServerKickMessage(server.getName())));
                continue;
            }
            this.audiences.player(player).sendMessage(this.settingsProxy.getMessage("singleMaintenanceActivated", "%SERVER%", server.getName()));
        }
    }

    @Override
    protected void kickPlayersTo(eu.kennytv.maintenance.api.proxy.Server server) {
        ServerInfo serverInfo = ((BungeeServer)server).getServer();
        for (ProxiedPlayer player : this.getProxy().getPlayers()) {
            if (this.hasPermission((CommandSender)player, "bypass") || this.settingsProxy.isWhitelisted(player.getUniqueId()) || player.getServer() != null && player.getServer().getInfo().getName().equals(serverInfo.getName())) continue;
            if (serverInfo.canAccess((CommandSender)player) && !this.isMaintenance(serverInfo)) {
                this.audiences.player(player).sendMessage(this.settingsProxy.getMessage("sentToWaitingServer", "%SERVER%", server.getName()));
                player.connect(serverInfo);
                continue;
            }
            player.disconnect(ComponentUtil.toBadComponents(this.settingsProxy.getKickMessage()));
        }
    }

    @Override
    public Task startMaintenanceRunnable(Runnable runnable) {
        return new BungeeTask(this.getProxy().getScheduler().schedule((Plugin)this.plugin, runnable, 0L, 1L, TimeUnit.SECONDS).getId());
    }

    @Override
    public CompletableFuture<@Nullable SenderInfo> getOfflinePlayer(String name) {
        ProxiedPlayer player = this.getProxy().getPlayer(name);
        if (player != null) {
            return CompletableFuture.completedFuture(new BungeeSenderInfo((CommandSender)player));
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                ProfileLookup profile = this.doUUIDLookup(name);
                return new ProxyOfflineSenderInfo(profile.getUuid(), profile.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<@Nullable SenderInfo> getOfflinePlayer(UUID uuid) {
        ProxiedPlayer player = this.getProxy().getPlayer(uuid);
        return CompletableFuture.completedFuture(player != null ? new BungeeSenderInfo((CommandSender)player) : null);
    }

    @Override
    @Nullable
    public eu.kennytv.maintenance.api.proxy.Server getServer(String server) {
        ServerInfo serverInfo = this.getProxy().getServerInfo(server);
        return serverInfo != null ? new BungeeServer(serverInfo) : null;
    }

    @Override
    public Set<String> getServers() {
        return this.getProxy().getServers().keySet();
    }

    @Override
    @Nullable
    public String getServerNameOf(SenderInfo sender) {
        ProxiedPlayer player = this.getProxy().getPlayer(sender.getUuid());
        if (player == null || player.getServer() == null) {
            return null;
        }
        return player.getServer().getInfo().getName();
    }

    @Override
    public void async(Runnable runnable) {
        this.getProxy().getScheduler().runAsync((Plugin)this.plugin, runnable);
    }

    @Override
    protected void executeConsoleCommand(String command) {
        this.getProxy().getPluginManager().dispatchCommand(this.getProxy().getConsole(), command);
    }

    @Override
    public void broadcast(Component component) {
        this.audiences.all().sendMessage(component);
    }

    @Override
    public File getDataFolder() {
        return this.plugin.getDataFolder();
    }

    @Override
    public File getPluginFile() {
        return this.plugin.getPluginFile();
    }

    @Override
    protected int getOnlinePlayers() {
        return this.getProxy().getOnlineCount();
    }

    @Override
    protected int getMaxPlayers() {
        return this.getProxy().getConfig().getPlayerLimit();
    }

    @Override
    public InputStream getResource(String name) {
        return this.plugin.getResourceAsStream(name);
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public String getServerVersion() {
        return this.getProxy().getVersion();
    }

    @Override
    public List<PluginDump> getPlugins() {
        return this.getProxy().getPluginManager().getPlugins().stream().map(plugin -> new PluginDump(plugin.getDescription().getName(), plugin.getDescription().getVersion(), Arrays.asList(plugin.getDescription().getAuthor()))).collect(Collectors.toList());
    }

    @Override
    protected void loadIcon(File file) throws IOException {
        this.favicon = Favicon.create((BufferedImage)ImageIO.read(file));
    }

    public boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission("maintenance." + permission) || sender.hasPermission("maintenance.admin");
    }

    public ProxyServer getProxy() {
        return this.plugin.getProxy();
    }

    public Favicon getFavicon() {
        return this.favicon;
    }

    public BungeeAudiences audiences() {
        return this.audiences;
    }

    private static /* synthetic */ String lambda$new$0(ProxiedPlayer player) {
        Server server = player.getServer();
        return server != null ? server.getInfo().getName() : null;
    }
}

