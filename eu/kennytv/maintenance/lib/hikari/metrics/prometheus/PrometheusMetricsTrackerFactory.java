/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.prometheus.client.Collector
 *  io.prometheus.client.CollectorRegistry
 */
package eu.kennytv.maintenance.lib.hikari.metrics.prometheus;

import eu.kennytv.maintenance.lib.hikari.metrics.IMetricsTracker;
import eu.kennytv.maintenance.lib.hikari.metrics.MetricsTrackerFactory;
import eu.kennytv.maintenance.lib.hikari.metrics.PoolStats;
import eu.kennytv.maintenance.lib.hikari.metrics.prometheus.HikariCPCollector;
import eu.kennytv.maintenance.lib.hikari.metrics.prometheus.PrometheusMetricsTracker;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrometheusMetricsTrackerFactory
implements MetricsTrackerFactory {
    private static final Map<CollectorRegistry, RegistrationStatus> registrationStatuses = new ConcurrentHashMap<CollectorRegistry, RegistrationStatus>();
    private final HikariCPCollector collector = new HikariCPCollector();
    private final CollectorRegistry collectorRegistry;

    public PrometheusMetricsTrackerFactory() {
        this(CollectorRegistry.defaultRegistry);
    }

    public PrometheusMetricsTrackerFactory(CollectorRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
    }

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        this.registerCollector(this.collector, this.collectorRegistry);
        this.collector.add(poolName, poolStats);
        return new PrometheusMetricsTracker(poolName, this.collectorRegistry, this.collector);
    }

    private void registerCollector(Collector collector, CollectorRegistry collectorRegistry) {
        if (registrationStatuses.putIfAbsent(collectorRegistry, RegistrationStatus.REGISTERED) == null) {
            collector.register(collectorRegistry);
        }
    }

    static enum RegistrationStatus {
        REGISTERED;

    }
}

