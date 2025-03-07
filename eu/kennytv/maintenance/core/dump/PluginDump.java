/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.dump;

import java.util.List;

public final class PluginDump {
    private final String name;
    private final String version;
    private final List<String> authors;

    public PluginDump(String name, String version, List<String> authors) {
        this.name = name;
        this.version = version;
        this.authors = authors != null && authors.isEmpty() ? null : authors;
    }
}

