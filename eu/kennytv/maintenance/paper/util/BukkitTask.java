/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package eu.kennytv.maintenance.paper.util;

import eu.kennytv.maintenance.core.util.Task;
import org.bukkit.Bukkit;

public final class BukkitTask
implements Task {
    private final int id;

    public BukkitTask(int id) {
        this.id = id;
    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.id);
    }
}

