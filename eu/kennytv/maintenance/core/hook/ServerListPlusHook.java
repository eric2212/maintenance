/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecrell.serverlistplus.core.ServerListPlusCore
 *  net.minecrell.serverlistplus.core.plugin.ServerListPlusPlugin
 */
package eu.kennytv.maintenance.core.hook;

import net.minecrell.serverlistplus.core.ServerListPlusCore;
import net.minecrell.serverlistplus.core.plugin.ServerListPlusPlugin;

public final class ServerListPlusHook {
    private final ServerListPlusCore serverListPlusCore;

    public ServerListPlusHook(Object serverListPlus) {
        if (!(serverListPlus instanceof ServerListPlusPlugin)) {
            throw new IllegalArgumentException("Couldn't parse ServerListPlus instance!");
        }
        this.serverListPlusCore = ((ServerListPlusPlugin)serverListPlus).getCore();
    }

    public void setEnabled(boolean enable) {
        this.serverListPlusCore.getProfiles().setEnabled(enable);
    }
}

