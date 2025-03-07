/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.micrometer.core.instrument.MeterRegistry
 */
package eu.kennytv.maintenance.lib.hikari.metrics.micrometer;

import eu.kennytv.maintenance.lib.hikari.metrics.IMetricsTracker;
import eu.kennytv.maintenance.lib.hikari.metrics.MetricsTrackerFactory;
import eu.kennytv.maintenance.lib.hikari.metrics.PoolStats;
import eu.kennytv.maintenance.lib.hikari.metrics.micrometer.MicrometerMetricsTracker;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerMetricsTrackerFactory
implements MetricsTrackerFactory {
    private final MeterRegistry registry;

    public MicrometerMetricsTrackerFactory(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        return new MicrometerMetricsTracker(poolName, poolStats, this.registry);
    }
}

