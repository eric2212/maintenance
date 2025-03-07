/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.util;

public final class MaintenanceVersion {
    public static final String VERSION = "4.3.0";
    private static final String IMPLEMENTATION_VERSION = "git-Maintenance-4.3.0:a9d052e";

    public static String getVersion() {
        return VERSION;
    }

    public static String getImplementationVersion() {
        return IMPLEMENTATION_VERSION;
    }
}

