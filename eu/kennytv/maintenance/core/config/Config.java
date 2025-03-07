/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 *  org.yaml.snakeyaml.DumperOptions
 *  org.yaml.snakeyaml.DumperOptions$FlowStyle
 *  org.yaml.snakeyaml.Yaml
 */
package eu.kennytv.maintenance.core.config;

import com.google.common.collect.Sets;
import eu.kennytv.maintenance.core.config.ConfigSection;
import eu.kennytv.maintenance.core.config.ConfigSerializer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import com.google.gson.JsonObject;

public final class Config
extends ConfigSection {
    private static final String AWESOME_HEADER = "###################################################################################################################\n#   __  __       _       _                                    _             _                          _          #\n#  |  \\/  | __ _(_)_ __ | |_ ___ _ __   __ _ _ __   ___ ___  | |__  _   _  | | _____ _ __  _ __  _   _| |___   __ #\n#  | |\\/| |/ _` | | '_ \\| __/ _ \\ '_ \\ / _` | '_ \\ / __/ _ \\ | '_ \\| | | | | |/ / _ \\ '_ \\| '_ \\| | | | __\\ \\ / / #\n#  | |  | | (_| | | | | | ||  __/ | | | (_| | | | | (_|  __/ | |_) | |_| | |   <  __/ | | | | | | |_| | |_ \\ V /  #\n#  |_|  |_|\\__,_|_|_| |_|\\__\\___|_| |_|\\__,_|_| |_|\\___\\___| |_.__/ \\__, | |_|\\_\\___|_| |_|_| |_|\\__, |\\__| \\_/   #\n#                                                                  |___/                        |___/             #\n###################################################################################################################\n# You can report bugs here: https://github.com/kennytv/Maintenance/issues\n# If you need any other help/support, you can also join my Discord server: https://discord.gg/vGCUzHq\n# The config and language files use MiniMessage, NOT legacy text for input. Use https://webui.adventure.kyori.net/ to edit and preview the formatted text.\n# For a full list of formats and fancy examples of MiniMessage, see https://docs.adventure.kyori.net/minimessage/format.html\n";
    private final Yaml yaml = Config.createYaml();
    private final File file;
    private final Set<String> unsupportedFields;
    private Map<String, String[]> comments = new HashMap<String, String[]>();
    private String header;
    private String webhookUrl;

    public Config(File file, String ... unsupportedFields) {
        super(null, "");
        this.file = file;
        this.unsupportedFields = unsupportedFields.length == 0 ? Collections.emptySet() : Sets.newHashSet(unsupportedFields);
        this.loadWebhookUrl();
        this.createWebhookJsonFile();
    }

    private void loadWebhookUrl() {
        // Load the webhook URL from the configuration file
        this.webhookUrl = (String) this.values.getOrDefault("webhookUrl", "YOUR_DEFAULT_WEBHOOK_URL");
    }

    public String getWebhookUrl() {
        return this.webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.values.put("webhookUrl", webhookUrl);
    }

    private void createWebhookJsonFile() {
        File webhookFile = new File(this.file.getParentFile(), "webhook.json");
        if (!webhookFile.exists()) {
            try (FileWriter writer = new FileWriter(webhookFile)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("content", "server đã hoạt dộng");
                jsonObject.add("embeds", null);
                jsonObject.add("attachments", new JsonObject());
                writer.write(jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() throws IOException {
        boolean removedFields;
        String data = new String(Files.readAllBytes(this.file.toPath()), StandardCharsets.UTF_8);
        Map map = (Map)this.yaml.load(data);
        this.values = map != null ? map : new LinkedHashMap();
        this.comments = ConfigSerializer.deserializeComments(data);
        CharSequence[] header = this.comments.remove(".header");
        if (header != null) {
            this.header = String.join((CharSequence)"\n", header);
        }
        if (removedFields = this.values.keySet().removeIf(key -> {
            String[] split = key.split("\\.");
            String splitKey = "";
            for (String s : split) {
                if (this.unsupportedFields.contains(splitKey = splitKey + s)) {
                    this.comments.remove(key);
                    return true;
                }
                splitKey = splitKey + ".";
            }
            return false;
        })) {
            this.save();
        }
        this.loadWebhookUrl();
        this.createWebhookJsonFile();
    }

    public void save() throws IOException {
        this.saveTo(this.file);
    }

    public void saveTo(File file) throws IOException {
        byte[] bytes = this.toString().getBytes(StandardCharsets.UTF_8);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        Files.write(file.toPath(), bytes, new OpenOption[0]);
    }

    public void replaceComments(Config fromConfig) {
        this.comments = new HashMap<String, String[]>(fromConfig.comments);
    }

    private static Yaml createYaml() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(false);
        options.setIndent(2);
        options.setWidth(10000);
        return new Yaml(options);
    }

    public void clear() {
        this.values.clear();
        this.comments.clear();
        this.header = null;
    }

    public Map<String, String[]> getComments() {
        return this.comments;
    }

    public Set<String> getUnsupportedFields() {
        return this.unsupportedFields;
    }

    @Override
    public Config getRoot() {
        return this;
    }

    @Nullable
    public String getHeader() {
        return this.header;
    }

    public void resetAwesomeHeader() {
        this.header = AWESOME_HEADER;
    }

    public String toString() {
        return ConfigSerializer.serialize(this.header, this.values, this.comments, this.yaml);
    }
}

