/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.api;

import java.util.Map;
import java.util.UUID;

public interface Settings {
    public boolean isMaintenance();

    public boolean isEnablePingMessages();

    public boolean isJoinNotifications();

    public boolean hasCustomIcon();

    public Map<UUID, String> getWhitelistedPlayers();

    public boolean isWhitelisted(UUID var1);

    public boolean removeWhitelistedPlayer(UUID var1);

    @Deprecated
    public boolean removeWhitelistedPlayer(String var1);

    public boolean addWhitelistedPlayer(UUID var1, String var2);

    public void reloadConfigs();

    public boolean debugEnabled();
}

