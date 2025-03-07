/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.MetricRegistry
 */
package eu.kennytv.maintenance.lib.hikari.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import eu.kennytv.maintenance.lib.hikari.metrics.IMetricsTracker;
import eu.kennytv.maintenance.lib.hikari.metrics.MetricsTrackerFactory;
import eu.kennytv.maintenance.lib.hikari.metrics.PoolStats;
import eu.kennytv.maintenance.lib.hikari.metrics.dropwizard.CodaHaleMetricsTracker;

public final class CodahaleMetricsTrackerFactory
implements MetricsTrackerFactory {
    private final MetricRegistry registry;

    public CodahaleMetricsTrackerFactory(MetricRegistry registry) {
        this.registry = registry;
    }

    public MetricRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        return new CodaHaleMetricsTracker(poolName, poolStats, this.registry);
    }
}

