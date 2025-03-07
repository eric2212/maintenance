/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.hikari.metrics;

import eu.kennytv.maintenance.lib.hikari.metrics.IMetricsTracker;
import eu.kennytv.maintenance.lib.hikari.metrics.PoolStats;

public interface MetricsTrackerFactory {
    public IMetricsTracker create(String var1, PoolStats var2);
}

