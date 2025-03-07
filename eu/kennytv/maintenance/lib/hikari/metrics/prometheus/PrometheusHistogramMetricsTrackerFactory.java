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
import eu.kennytv.maintenance.lib.hikari.metrics.prometheus.PrometheusHistogramMetricsTracker;
import eu.kennytv.maintenance.lib.hikari.metrics.prometheus.PrometheusMetricsTrackerFactory;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrometheusHistogramMetricsTrackerFactory
implements MetricsTrackerFactory {
    private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses = new ConcurrentHashMap<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus>();
    private final HikariCPCollector collector = new HikariCPCollector();
    private final CollectorRegistry collectorRegistry;

    public PrometheusHistogramMetricsTrackerFactory() {
        this(CollectorRegistry.defaultRegistry);
    }

    public PrometheusHistogramMetricsTrackerFactory(CollectorRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
    }

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        this.registerCollector(this.collector, this.collectorRegistry);
        this.collector.add(poolName, poolStats);
        return new PrometheusHistogramMetricsTracker(poolName, this.collectorRegistry, this.collector);
    }

    private void registerCollector(Collector collector, CollectorRegistry collectorRegistry) {
        if (registrationStatuses.putIfAbsent(collectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null) {
            collector.register(collectorRegistry);
        }
    }
}

