/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.runnable;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.runnable.MaintenanceRunnableBase;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MaintenanceRunnable
extends MaintenanceRunnableBase {
    public MaintenanceRunnable(MaintenancePlugin plugin, Settings settings, int seconds, boolean enable) {
        super(plugin, settings, seconds, enable);
    }

    private void sendWebhook(String message) {
        try {
            String webhookUrl = this.settings.getWebhookUrl();
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            // Read JSON content from webhook.json
            JsonObject jsonObject = readJsonFromFile("path/to/webhook.json");
            jsonObject.addProperty("content", message);
            String jsonInputString = jsonObject.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            System.out.println("Webhook response code: " + code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonObject readJsonFromFile(String filePath) {
        try (FileReader reader = new FileReader(new File(filePath))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }

    @Override
    protected void finish() {
        this.plugin.setMaintenance(this.enable);
        sendWebhook("Maintenance has ended.");
    }

    @Override
    protected Component getStartMessage() {
        sendWebhook("Maintenance is starting.");
        return this.settings.getMessage("starttimerBroadcast", "%TIME%", this.getTime());
    }

    @Override
    protected Component getEndMessage() {
        return this.settings.getMessage("endtimerBroadcast", "%TIME%", this.getTime());
    }
}

