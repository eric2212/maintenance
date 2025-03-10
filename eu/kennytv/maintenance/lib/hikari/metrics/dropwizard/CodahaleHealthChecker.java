/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.MetricRegistry
 *  com.codahale.metrics.Timer
 *  com.codahale.metrics.health.HealthCheck
 *  com.codahale.metrics.health.HealthCheck$Result
 *  com.codahale.metrics.health.HealthCheckRegistry
 */
package eu.kennytv.maintenance.lib.hikari.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import eu.kennytv.maintenance.lib.hikari.HikariConfig;
import eu.kennytv.maintenance.lib.hikari.pool.HikariPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

public final class CodahaleHealthChecker {
    public static void registerHealthChecks(HikariPool pool, HikariConfig hikariConfig, HealthCheckRegistry registry) {
        SortedMap timers;
        Properties healthCheckProperties = hikariConfig.getHealthCheckProperties();
        MetricRegistry metricRegistry = (MetricRegistry)hikariConfig.getMetricRegistry();
        long checkTimeoutMs = Long.parseLong(healthCheckProperties.getProperty("connectivityCheckTimeoutMs", String.valueOf(hikariConfig.getConnectionTimeout())));
        registry.register(MetricRegistry.name((String)hikariConfig.getPoolName(), (String[])new String[]{"pool", "ConnectivityCheck"}), (HealthCheck)new ConnectivityHealthCheck(pool, checkTimeoutMs));
        long expected99thPercentile = Long.parseLong(healthCheckProperties.getProperty("expected99thPercentileMs", "0"));
        if (metricRegistry != null && expected99thPercentile > 0L && !(timers = metricRegistry.getTimers((name, metric) -> name.equals(MetricRegistry.name((String)hikariConfig.getPoolName(), (String[])new String[]{"pool", "Wait"})))).isEmpty()) {
            Timer timer = (Timer)timers.entrySet().iterator().next().getValue();
            registry.register(MetricRegistry.name((String)hikariConfig.getPoolName(), (String[])new String[]{"pool", "Connection99Percent"}), (HealthCheck)new Connection99Percent(timer, expected99thPercentile));
        }
    }

    private CodahaleHealthChecker() {
    }

    private static class Connection99Percent
    extends HealthCheck {
        private final Timer waitTimer;
        private final long expected99thPercentile;

        Connection99Percent(Timer waitTimer, long expected99thPercentile) {
            this.waitTimer = waitTimer;
            this.expected99thPercentile = expected99thPercentile;
        }

        protected HealthCheck.Result check() throws Exception {
            long the99thPercentile = TimeUnit.NANOSECONDS.toMillis(Math.round(this.waitTimer.getSnapshot().get99thPercentile()));
            return the99thPercentile <= this.expected99thPercentile ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy((String)"99th percentile connection wait time of %dms exceeds the threshold %dms", (Object[])new Object[]{the99thPercentile, this.expected99thPercentile});
        }
    }

    private static class ConnectivityHealthCheck
    extends HealthCheck {
        private final HikariPool pool;
        private final long checkTimeoutMs;

        ConnectivityHealthCheck(HikariPool pool, long checkTimeoutMs) {
            this.pool = pool;
            this.checkTimeoutMs = checkTimeoutMs > 0L && checkTimeoutMs != Integer.MAX_VALUE ? checkTimeoutMs : TimeUnit.SECONDS.toMillis(10L);
        }

        protected HealthCheck.Result check() throws Exception {
            HealthCheck.Result result;
            block8: {
                Connection connection = this.pool.getConnection(this.checkTimeoutMs);
                try {
                    result = HealthCheck.Result.healthy();
                    if (connection == null) break block8;
                } catch (Throwable throwable) {
                    try {
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    } catch (SQLException e) {
                        return HealthCheck.Result.unhealthy((Throwable)e);
                    }
                }
                connection.close();
            }
            return result;
        }
    }
}

