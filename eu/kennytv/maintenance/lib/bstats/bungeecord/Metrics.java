/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.plugin.Plugin
 *  net.md_5.bungee.config.Configuration
 *  net.md_5.bungee.config.ConfigurationProvider
 *  net.md_5.bungee.config.YamlConfiguration
 */
package eu.kennytv.maintenance.lib.bstats.bungeecord;

import eu.kennytv.maintenance.lib.bstats.MetricsBase;
import eu.kennytv.maintenance.lib.bstats.charts.CustomChart;
import eu.kennytv.maintenance.lib.bstats.json.JsonObjectBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Metrics {
    private final Plugin plugin;
    private final MetricsBase metricsBase;
    private boolean enabled;
    private String serverUUID;
    private boolean logErrors = false;
    private boolean logSentData;
    private boolean logResponseStatusText;

    public Metrics(Plugin plugin, int serviceId) {
        this.plugin = plugin;
        try {
            this.loadConfig();
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load bStats config!", e);
            this.metricsBase = null;
            return;
        }
        this.metricsBase = new MetricsBase("bungeecord", this.serverUUID, serviceId, this.enabled, this::appendPlatformData, this::appendServiceData, null, () -> true, (message, error) -> this.plugin.getLogger().log(Level.WARNING, (String)message, (Throwable)error), message -> this.plugin.getLogger().log(Level.INFO, (String)message), this.logErrors, this.logSentData, this.logResponseStatusText, false);
    }

    private void loadConfig() throws IOException {
        File bStatsFolder = new File(this.plugin.getDataFolder().getParentFile(), "bStats");
        bStatsFolder.mkdirs();
        File configFile = new File(bStatsFolder, "config.yml");
        if (!configFile.exists()) {
            this.writeFile(configFile, "# bStats (https://bStats.org) thu thập một số thông tin cơ bản cho tác giả plugin, chẳng hạn như "# nhiều người sử dụng plugin của họ và tổng số người chơi của họ. Nên giữ bStats", "# bật, nhưng nếu bạn không thoải mái với điều này, bạn có thể tắt cài đặt này. Không có", "# hình phạt hiệu suất liên quan đến việc bật số liệu thống kê và dữ liệu được gửi đến bStats được hoàn toàn", "# anonymous.", "enabled: true", "serverUuid: \"" + UUID.randomUUID() + "\"", "logFailedRequests: false", "logSentData: false", "logResponseStatusText: false");
        }
        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        this.enabled = configuration.getBoolean("enabled", true);
        this.serverUUID = configuration.getString("serverUuid");
        this.logErrors = configuration.getBoolean("logFailedRequests", false);
        this.logSentData = configuration.getBoolean("logSentData", false);
        this.logResponseStatusText = configuration.getBoolean("logResponseStatusText", false);
    }

    private void writeFile(File file, String ... lines) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));){
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
    }

    public void shutdown() {
        this.metricsBase.shutdown();
    }

    public void addCustomChart(CustomChart chart) {
        this.metricsBase.addCustomChart(chart);
    }

    private void appendPlatformData(JsonObjectBuilder builder) {
        builder.appendField("playerAmount", this.plugin.getProxy().getOnlineCount());
        builder.appendField("managedServers", this.plugin.getProxy().getServers().size());
        builder.appendField("onlineMode", this.plugin.getProxy().getConfig().isOnlineMode() ? 1 : 0);
        builder.appendField("bungeecordVersion", this.plugin.getProxy().getVersion());
        builder.appendField("bungeecordName", this.plugin.getProxy().getName());
        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", this.plugin.getDescription().getVersion());
    }
}

