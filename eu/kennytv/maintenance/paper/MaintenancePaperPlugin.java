/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.util.CachedServerIcon
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.paper;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.dump.PluginDump;
import eu.kennytv.maintenance.core.hook.ServerListPlusHook;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.core.util.ServerType;
import eu.kennytv.maintenance.core.util.Task;
import eu.kennytv.maintenance.lib.bstats.bukkit.Metrics;
import eu.kennytv.maintenance.lib.kyori.adventure.platform.bukkit.BukkitAudiences;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.paper.MaintenancePaperBase;
import eu.kennytv.maintenance.paper.command.MaintenancePaperCommand;
import eu.kennytv.maintenance.paper.listener.PaperServerListPingListener;
import eu.kennytv.maintenance.paper.listener.PlayerLoginListener;
import eu.kennytv.maintenance.paper.listener.ServerInfoPacketListener;
import eu.kennytv.maintenance.paper.listener.ServerListPingListener;
import eu.kennytv.maintenance.paper.util.BukkitOfflinePlayerInfo;
import eu.kennytv.maintenance.paper.util.BukkitTask;
import eu.kennytv.maintenance.paper.util.ComponentUtil;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.Nullable;

public final class MaintenancePaperPlugin
extends MaintenancePlugin {
    private static final boolean FOLIA = MaintenancePaperPlugin.hasClass("io.papermc.paper.threadedregions.RegionizedServer");
    private final MaintenancePaperBase plugin;
    private final BukkitAudiences audiences;
    private CachedServerIcon favicon;

    MaintenancePaperPlugin(MaintenancePaperBase plugin) {
        super(plugin.getDescription().getVersion(), ServerType.SPIGOT);
        this.plugin = plugin;
        this.audiences = BukkitAudiences.create((Plugin)plugin);
        this.settings = new Settings(this, "mysql", "proxied-maintenance-servers", "fallback", "waiting-server", "commands-on-single-maintenance-enable", "commands-on-single-maintenance-disable");
        this.sendEnableMessage();
        MaintenancePaperCommand command = new MaintenancePaperCommand(this, this.settings);
        this.commandManager = command;
        plugin.getCommand("maintenance").setExecutor((CommandExecutor)command);
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents((Listener)new PlayerLoginListener(this, this.settings), (Plugin)plugin);
        if (this.canUsePaperListener()) {
            pm.registerEvents((Listener)new PaperServerListPingListener(this, this.settings), (Plugin)plugin);
        } else if (pm.isPluginEnabled("ProtocolLib")) {
            pm.registerEvents((Listener)new ServerInfoPacketListener(this, plugin, this.settings), (Plugin)plugin);
        } else {
            pm.registerEvents((Listener)new ServerListPingListener(this, this.settings), (Plugin)plugin);
            this.getLogger().warning("To use this plugin on Spigot to its full extent, you need the plugin ProtocolLib!");
        }
        this.continueLastEndtimer();
        new Metrics((Plugin)plugin, 2205);
        Plugin serverListPlus = pm.getPlugin("ServerListPlus");
        if (pm.isPluginEnabled(serverListPlus)) {
            this.serverListPlusHook = new ServerListPlusHook(serverListPlus);
            if (this.settings.isEnablePingMessages()) {
                this.serverListPlusHook.setEnabled(!this.settings.isMaintenance());
            }
            plugin.getLogger().info("Enabled ServerListPlus integration");
        }
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private boolean canUsePaperListener() {
        try {
            Class.forName("com.destroystokyo.paper.event.server.PaperServerListPingEvent");
            if (this.getServer().getPluginManager().isPluginEnabled("ProtocolSupport")) {
                this.getLogger().warning("Found ProtocolSupport - switching to ProtocolLib packet adapter, as PS does not fire Paper's ping event");
                return false;
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Task startMaintenanceRunnable(Runnable runnable) {
        if (FOLIA) {
            throw new UnsupportedOperationException("Scheduling tasks is not yet supported on Folia");
        }
        return new BukkitTask(this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this.plugin, runnable, 0L, 20L));
    }

    @Override
    public CompletableFuture<@Nullable SenderInfo> getOfflinePlayer(String name) {
        OfflinePlayer player = this.getServer().getOfflinePlayer(name);
        return CompletableFuture.completedFuture(player.getName() != null ? new BukkitOfflinePlayerInfo(player) : null);
    }

    @Override
    public CompletableFuture<@Nullable SenderInfo> getOfflinePlayer(UUID uuid) {
        OfflinePlayer player = this.getServer().getOfflinePlayer(uuid);
        return CompletableFuture.completedFuture(player.getName() != null ? new BukkitOfflinePlayerInfo(player) : null);
    }

    @Override
    public void async(Runnable runnable) {
        if (FOLIA) {
            runnable.run();
            return;
        }
        this.getServer().getScheduler().runTaskAsynchronously((Plugin)this.plugin, runnable);
    }

    @Override
    protected void executeConsoleCommand(String command) {
        this.getServer().dispatchCommand((CommandSender)this.getServer().getConsoleSender(), command);
    }

    @Override
    public void broadcast(Component component) {
        this.audiences.all().sendMessage(component);
    }

    @Override
    protected void kickPlayers() {
        for (Player p : this.getServer().getOnlinePlayers()) {
            if (this.hasPermission((CommandSender)p, "bypass") || this.settings.isWhitelisted(p.getUniqueId())) continue;
            Component kickMessage = this.settings.getKickMessage();
            if (ComponentUtil.PAPER) {
                p.kick(ComponentUtil.toPaperComponent(kickMessage));
                continue;
            }
            p.kickPlayer(ComponentUtil.toLegacy(kickMessage));
        }
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
        return this.getServer().getOnlinePlayers().size();
    }

    @Override
    protected int getMaxPlayers() {
        return this.getServer().getMaxPlayers();
    }

    @Override
    public InputStream getResource(String name) {
        return this.plugin.getResource(name);
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public String getServerVersion() {
        return this.getServer().getVersion();
    }

    @Override
    public List<PluginDump> getPlugins() {
        return Arrays.stream(this.getServer().getPluginManager().getPlugins()).map(plugin -> new PluginDump(plugin.getDescription().getName(), plugin.getDescription().getVersion(), plugin.getDescription().getAuthors())).collect(Collectors.toList());
    }

    @Override
    protected void loadIcon(File file) throws Exception {
        this.favicon = this.plugin.getServer().loadServerIcon(ImageIO.read(file));
    }

    public boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission("maintenance." + permission) || sender.hasPermission("maintenance.admin");
    }

    public Server getServer() {
        return this.plugin.getServer();
    }

    public CachedServerIcon getFavicon() {
        return this.favicon;
    }

    public BukkitAudiences audiences() {
        return this.audiences;
    }
}

