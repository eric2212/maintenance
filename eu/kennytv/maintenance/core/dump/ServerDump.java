/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.dump;

import java.util.List;

public final class ServerDump {
    private final String pluginVersion;
    private final String platform;
    private final String serverVersion;
    private final List<String> maintenance;

    public ServerDump(String pluginVersion, String platform, String serverVersion, List<String> maintenance) {
        this.pluginVersion = pluginVersion;
        this.platform = platform;
        this.serverVersion = serverVersion;
        this.maintenance = maintenance;
    }
}

