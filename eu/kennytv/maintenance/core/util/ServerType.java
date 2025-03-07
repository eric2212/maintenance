/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.util;

public enum ServerType {
    BUNGEE("Bungee"),
    SPIGOT("Spigot"),
    SPONGE("Sponge"),
    VELOCITY("Velocity");

    private final String name;

    private ServerType(String name) {
        this.name = name;
    }

    public boolean isProxy() {
        return this == BUNGEE || this == VELOCITY;
    }

    public String toString() {
        return this.name;
    }
}

