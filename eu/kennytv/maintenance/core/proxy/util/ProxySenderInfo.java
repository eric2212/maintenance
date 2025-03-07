/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.util;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;

public interface ProxySenderInfo
extends SenderInfo {
    public boolean canAccess(Server var1);

    public void disconnect(Component var1);
}

