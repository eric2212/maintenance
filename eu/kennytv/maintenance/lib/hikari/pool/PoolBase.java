/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.hikari.pool;

import eu.kennytv.maintenance.lib.hikari.HikariConfig;
import eu.kennytv.maintenance.lib.hikari.SQLExceptionOverride;
import eu.kennytv.maintenance.lib.hikari.metrics.IMetricsTracker;
import eu.kennytv.maintenance.lib.hikari.pool.HikariPool;
import eu.kennytv.maintenance.lib.hikari.pool.PoolEntry;
import eu.kennytv.maintenance.lib.hikari.pool.ProxyConnection;
import eu.kennytv.maintenance.lib.hikari.util.ClockSource;
import eu.kennytv.maintenance.lib.hikari.util.DriverDataSource;
import eu.kennytv.maintenance.lib.hikari.util.PropertyElf;
import eu.kennytv.maintenance.lib.hikari.util.UtilityElf;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class PoolBase {
    private final Logger logger = LoggerFactory.getLogger(PoolBase.class);
    public final HikariConfig config;
    IMetricsTrackerDelegate metricsTracker;
    protected final String poolName;
    volatile String catalog;
    final AtomicReference<Exception> lastConnectionFailure;
    long connectionTimeout;
    long validationTimeout;
    SQLExceptionOverride exceptionOverride;
    private static final String[] RESET_STATES = new String[]{"readOnly", "autoCommit", "isolation", "catalog", "netTimeout", "schema"};
    private static final int UNINITIALIZED = -1;
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private int networkTimeout;
    private int isNetworkTimeoutSupported;
    private int isQueryTimeoutSupported;
    private int defaultTransactionIsolation;
    private int transactionIsolation;
    private Executor netTimeoutExecutor;
    private DataSource dataSource;
    private final String schema;
    private final boolean isReadOnly;
    private final boolean isAutoCommit;
    private final boolean isUseJdbc4Validation;
    private final boolean isIsolateInternalQueries;
    private volatile boolean isValidChecked;

    PoolBase(HikariConfig config) {
        this.config = config;
        this.networkTimeout = -1;
        this.catalog = config.getCatalog();
        this.schema = config.getSchema();
        this.isReadOnly = config.isReadOnly();
        this.isAutoCommit = config.isAutoCommit();
        this.exceptionOverride = UtilityElf.createInstance(config.getExceptionOverrideClassName(), SQLExceptionOverride.class, new Object[0]);
        this.transactionIsolation = UtilityElf.getTransactionIsolation(config.getTransactionIsolation());
        this.isQueryTimeoutSupported = -1;
        this.isNetworkTimeoutSupported = -1;
        this.isUseJdbc4Validation = config.getConnectionTestQuery() == null;
        this.isIsolateInternalQueries = config.isIsolateInternalQueries();
        this.poolName = config.getPoolName();
        this.connectionTimeout = config.getConnectionTimeout();
        this.validationTimeout = config.getValidationTimeout();
        this.lastConnectionFailure = new AtomicReference();
        this.initializeDataSource();
    }

    public String toString() {
        return this.poolName;
    }

    abstract void recycle(PoolEntry var1);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void quietlyCloseConnection(Connection connection, String closureReason) {
        if (connection != null) {
            try {
                this.logger.debug("{} - Closing connection {}: {}", this.poolName, connection, closureReason);
                try {
                    this.setNetworkTimeout(connection, TimeUnit.SECONDS.toMillis(15L));
                } catch (SQLException sQLException) {
                } finally {
                    connection.close();
                }
            } catch (Exception e) {
                this.logger.debug("{} - Closing connection {} failed", this.poolName, connection, e);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    boolean isConnectionAlive(Connection connection) {
        try {
            try {
                this.setNetworkTimeout(connection, this.validationTimeout);
                int validationSeconds = (int)Math.max(1000L, this.validationTimeout) / 1000;
                if (this.isUseJdbc4Validation) {
                    boolean bl = connection.isValid(validationSeconds);
                    return bl;
                }
                try (Statement statement = connection.createStatement();){
                    if (this.isNetworkTimeoutSupported != 1) {
                        this.setQueryTimeout(statement, validationSeconds);
                    }
                    statement.execute(this.config.getConnectionTestQuery());
                    return true;
                }
            } finally {
                this.setNetworkTimeout(connection, this.networkTimeout);
                if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                    connection.rollback();
                }
            }
        } catch (Exception e) {
            this.lastConnectionFailure.set(e);
            this.logger.warn("{} - Failed to validate connection {} ({}). Possibly consider using a shorter maxLifetime value.", this.poolName, connection, e.getMessage());
            return false;
        }
    }

    Exception getLastConnectionFailure() {
        return this.lastConnectionFailure.get();
    }

    public DataSource getUnwrappedDataSource() {
        return this.dataSource;
    }

    PoolEntry newPoolEntry() throws Exception {
        return new PoolEntry(this.newConnection(), this, this.isReadOnly, this.isAutoCommit);
    }

    void resetConnectionState(Connection connection, ProxyConnection proxyConnection, int dirtyBits) throws SQLException {
        int resetBits = 0;
        if ((dirtyBits & 1) != 0 && proxyConnection.getReadOnlyState() != this.isReadOnly) {
            connection.setReadOnly(this.isReadOnly);
            resetBits |= 1;
        }
        if ((dirtyBits & 2) != 0 && proxyConnection.getAutoCommitState() != this.isAutoCommit) {
            connection.setAutoCommit(this.isAutoCommit);
            resetBits |= 2;
        }
        if ((dirtyBits & 4) != 0 && proxyConnection.getTransactionIsolationState() != this.transactionIsolation) {
            connection.setTransactionIsolation(this.transactionIsolation);
            resetBits |= 4;
        }
        if ((dirtyBits & 8) != 0 && this.catalog != null && !this.catalog.equals(proxyConnection.getCatalogState())) {
            connection.setCatalog(this.catalog);
            resetBits |= 8;
        }
        if ((dirtyBits & 0x10) != 0 && proxyConnection.getNetworkTimeoutState() != this.networkTimeout) {
            this.setNetworkTimeout(connection, this.networkTimeout);
            resetBits |= 0x10;
        }
        if ((dirtyBits & 0x20) != 0 && this.schema != null && !this.schema.equals(proxyConnection.getSchemaState())) {
            connection.setSchema(this.schema);
            resetBits |= 0x20;
        }
        if (resetBits != 0 && this.logger.isDebugEnabled()) {
            this.logger.debug("{} - Reset ({}) on connection {}", this.poolName, this.stringFromResetBits(resetBits), connection);
        }
    }

    void shutdownNetworkTimeoutExecutor() {
        if (this.netTimeoutExecutor instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor)this.netTimeoutExecutor).shutdownNow();
        }
    }

    long getLoginTimeout() {
        try {
            return this.dataSource != null ? (long)this.dataSource.getLoginTimeout() : TimeUnit.SECONDS.toSeconds(5L);
        } catch (SQLException e) {
            return TimeUnit.SECONDS.toSeconds(5L);
        }
    }

    void handleMBeans(HikariPool hikariPool, boolean register) {
        if (!this.config.isRegisterMbeans()) {
            return;
        }
        try {
            ObjectName beanPoolName;
            ObjectName beanConfigName;
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            if ("true".equals(System.getProperty("hikaricp.jmx.register2.0"))) {
                beanConfigName = new ObjectName("eu.kennytv.maintenance.lib.hikari:type=PoolConfig,name=" + this.poolName);
                beanPoolName = new ObjectName("eu.kennytv.maintenance.lib.hikari:type=Pool,name=" + this.poolName);
            } else {
                beanConfigName = new ObjectName("eu.kennytv.maintenance.lib.hikari:type=PoolConfig (" + this.poolName + ")");
                beanPoolName = new ObjectName("eu.kennytv.maintenance.lib.hikari:type=Pool (" + this.poolName + ")");
            }
            if (register) {
                if (!mBeanServer.isRegistered(beanConfigName)) {
                    mBeanServer.registerMBean(this.config, beanConfigName);
                    mBeanServer.registerMBean(hikariPool, beanPoolName);
                } else {
                    this.logger.error("{} - JMX name ({}) is already registered.", (Object)this.poolName, (Object)this.poolName);
                }
            } else if (mBeanServer.isRegistered(beanConfigName)) {
                mBeanServer.unregisterMBean(beanConfigName);
                mBeanServer.unregisterMBean(beanPoolName);
            }
        } catch (Exception e) {
            this.logger.warn("{} - Failed to {} management beans.", this.poolName, register ? "register" : "unregister", e);
        }
    }

    private void initializeDataSource() {
        String jdbcUrl = this.config.getJdbcUrl();
        String username = this.config.getUsername();
        String password = this.config.getPassword();
        String dsClassName = this.config.getDataSourceClassName();
        String driverClassName = this.config.getDriverClassName();
        String dataSourceJNDI = this.config.getDataSourceJNDI();
        Properties dataSourceProperties = this.config.getDataSourceProperties();
        DataSource ds = this.config.getDataSource();
        if (dsClassName != null && ds == null) {
            ds = UtilityElf.createInstance(dsClassName, DataSource.class, new Object[0]);
            PropertyElf.setTargetFromProperties(ds, dataSourceProperties);
        } else if (jdbcUrl != null && ds == null) {
            ds = new DriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
        } else if (dataSourceJNDI != null && ds == null) {
            try {
                InitialContext ic = new InitialContext();
                ds = (DataSource)ic.lookup(dataSourceJNDI);
            } catch (NamingException e) {
                throw new HikariPool.PoolInitializationException(e);
            }
        }
        if (ds != null) {
            this.setLoginTimeout(ds);
            this.createNetworkTimeoutExecutor(ds, dsClassName, jdbcUrl);
        }
        this.dataSource = ds;
    }

    private Connection newConnection() throws Exception {
        long start = ClockSource.currentTime();
        Connection connection = null;
        try {
            String username = this.config.getUsername();
            String password = this.config.getPassword();
            Connection connection2 = connection = username == null ? this.dataSource.getConnection() : this.dataSource.getConnection(username, password);
            if (connection == null) {
                throw new SQLTransientConnectionException("DataSource returned null unexpectedly");
            }
            this.setupConnection(connection);
            this.lastConnectionFailure.set(null);
            Connection connection3 = connection;
            return connection3;
        } catch (Exception e) {
            if (connection != null) {
                this.quietlyCloseConnection(connection, "(Failed to create/setup connection)");
            } else if (this.getLastConnectionFailure() == null) {
                this.logger.debug("{} - Failed to create/setup connection: {}", (Object)this.poolName, (Object)e.getMessage());
            }
            this.lastConnectionFailure.set(e);
            throw e;
        } finally {
            if (this.metricsTracker != null) {
                this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(start));
            }
        }
    }

    private void setupConnection(Connection connection) throws ConnectionSetupException {
        try {
            if (this.networkTimeout == -1) {
                this.networkTimeout = this.getAndSetNetworkTimeout(connection, this.validationTimeout);
            } else {
                this.setNetworkTimeout(connection, this.validationTimeout);
            }
            if (connection.isReadOnly() != this.isReadOnly) {
                connection.setReadOnly(this.isReadOnly);
            }
            if (connection.getAutoCommit() != this.isAutoCommit) {
                connection.setAutoCommit(this.isAutoCommit);
            }
            this.checkDriverSupport(connection);
            if (this.transactionIsolation != this.defaultTransactionIsolation) {
                connection.setTransactionIsolation(this.transactionIsolation);
            }
            if (this.catalog != null) {
                connection.setCatalog(this.catalog);
            }
            if (this.schema != null) {
                connection.setSchema(this.schema);
            }
            this.executeSql(connection, this.config.getConnectionInitSql(), true);
            this.setNetworkTimeout(connection, this.networkTimeout);
        } catch (SQLException e) {
            throw new ConnectionSetupException(e);
        }
    }

    private void checkDriverSupport(Connection connection) throws SQLException {
        if (!this.isValidChecked) {
            this.checkValidationSupport(connection);
            this.checkDefaultIsolation(connection);
            this.isValidChecked = true;
        }
    }

    private void checkValidationSupport(Connection connection) throws SQLException {
        try {
            if (this.isUseJdbc4Validation) {
                connection.isValid(1);
            } else {
                this.executeSql(connection, this.config.getConnectionTestQuery(), false);
            }
        } catch (AbstractMethodError | Exception e) {
            this.logger.error("{} - Failed to execute{} connection test query ({}).", this.poolName, this.isUseJdbc4Validation ? " isValid() for connection, configure" : "", e.getMessage());
            throw e;
        }
    }

    private void checkDefaultIsolation(Connection connection) throws SQLException {
        block3: {
            try {
                this.defaultTransactionIsolation = connection.getTransactionIsolation();
                if (this.transactionIsolation == -1) {
                    this.transactionIsolation = this.defaultTransactionIsolation;
                }
            } catch (SQLException e) {
                this.logger.warn("{} - Default transaction isolation level detection failed ({}).", (Object)this.poolName, (Object)e.getMessage());
                if (e.getSQLState() == null || e.getSQLState().startsWith("08")) break block3;
                throw e;
            }
        }
    }

    private void setQueryTimeout(Statement statement, int timeoutSec) {
        block3: {
            if (this.isQueryTimeoutSupported != 0) {
                try {
                    statement.setQueryTimeout(timeoutSec);
                    this.isQueryTimeoutSupported = 1;
                } catch (Exception e) {
                    if (this.isQueryTimeoutSupported != -1) break block3;
                    this.isQueryTimeoutSupported = 0;
                    this.logger.info("{} - Failed to set query timeout for statement. ({})", (Object)this.poolName, (Object)e.getMessage());
                }
            }
        }
    }

    private int getAndSetNetworkTimeout(Connection connection, long timeoutMs) {
        block4: {
            if (this.isNetworkTimeoutSupported != 0) {
                try {
                    int originalTimeout = connection.getNetworkTimeout();
                    connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
                    this.isNetworkTimeoutSupported = 1;
                    return originalTimeout;
                } catch (AbstractMethodError | Exception e) {
                    if (this.isNetworkTimeoutSupported != -1) break block4;
                    this.isNetworkTimeoutSupported = 0;
                    this.logger.info("{} - Driver does not support get/set network timeout for connections. ({})", (Object)this.poolName, (Object)e.getMessage());
                    if (this.validationTimeout < TimeUnit.SECONDS.toMillis(1L)) {
                        this.logger.warn("{} - A validationTimeout of less than 1 second cannot be honored on drivers without setNetworkTimeout() support.", (Object)this.poolName);
                    }
                    if (this.validationTimeout % TimeUnit.SECONDS.toMillis(1L) == 0L) break block4;
                    this.logger.warn("{} - A validationTimeout with fractional second granularity cannot be honored on drivers without setNetworkTimeout() support.", (Object)this.poolName);
                }
            }
        }
        return 0;
    }

    private void setNetworkTimeout(Connection connection, long timeoutMs) throws SQLException {
        if (this.isNetworkTimeoutSupported == 1) {
            connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
        }
    }

    private void executeSql(Connection connection, String sql, boolean isCommit) throws SQLException {
        if (sql != null) {
            try (Statement statement = connection.createStatement();){
                statement.execute(sql);
            }
            if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                if (isCommit) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            }
        }
    }

    private void createNetworkTimeoutExecutor(DataSource dataSource, String dsClassName, String jdbcUrl) {
        if (dsClassName != null && dsClassName.contains("Mysql") || jdbcUrl != null && jdbcUrl.contains("mysql") || dataSource != null && dataSource.getClass().getName().contains("Mysql")) {
            this.netTimeoutExecutor = new SynchronousExecutor();
        } else {
            ThreadFactory threadFactory = this.config.getThreadFactory();
            threadFactory = threadFactory != null ? threadFactory : new UtilityElf.DefaultThreadFactory(this.poolName + " network timeout executor", true);
            ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool(threadFactory);
            executor.setKeepAliveTime(15L, TimeUnit.SECONDS);
            executor.allowCoreThreadTimeOut(true);
            this.netTimeoutExecutor = executor;
        }
    }

    private void setLoginTimeout(DataSource dataSource) {
        if (this.connectionTimeout != Integer.MAX_VALUE) {
            try {
                dataSource.setLoginTimeout(Math.max(1, (int)TimeUnit.MILLISECONDS.toSeconds(500L + this.connectionTimeout)));
            } catch (Exception e) {
                this.logger.info("{} - Failed to set login timeout for data source. ({})", (Object)this.poolName, (Object)e.getMessage());
            }
        }
    }

    private String stringFromResetBits(int bits) {
        StringBuilder sb = new StringBuilder();
        for (int ndx = 0; ndx < RESET_STATES.length; ++ndx) {
            if ((bits & 1 << ndx) == 0) continue;
            sb.append(RESET_STATES[ndx]).append(", ");
        }
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    static final class NopMetricsTrackerDelegate
    implements IMetricsTrackerDelegate {
        NopMetricsTrackerDelegate() {
        }
    }

    static class MetricsTrackerDelegate
    implements IMetricsTrackerDelegate {
        final IMetricsTracker tracker;

        MetricsTrackerDelegate(IMetricsTracker tracker) {
            this.tracker = tracker;
        }

        @Override
        public void recordConnectionUsage(PoolEntry poolEntry) {
            this.tracker.recordConnectionUsageMillis(poolEntry.getMillisSinceBorrowed());
        }

        @Override
        public void recordConnectionCreated(long connectionCreatedMillis) {
            this.tracker.recordConnectionCreatedMillis(connectionCreatedMillis);
        }

        @Override
        public void recordBorrowTimeoutStats(long startTime) {
            this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime));
        }

        @Override
        public void recordBorrowStats(PoolEntry poolEntry, long startTime) {
            long now;
            poolEntry.lastBorrowed = now = ClockSource.currentTime();
            this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime, now));
        }

        @Override
        public void recordConnectionTimeout() {
            this.tracker.recordConnectionTimeout();
        }

        @Override
        public void close() {
            this.tracker.close();
        }
    }

    static interface IMetricsTrackerDelegate
    extends AutoCloseable {
        default public void recordConnectionUsage(PoolEntry poolEntry) {
        }

        default public void recordConnectionCreated(long connectionCreatedMillis) {
        }

        default public void recordBorrowTimeoutStats(long startTime) {
        }

        default public void recordBorrowStats(PoolEntry poolEntry, long startTime) {
        }

        default public void recordConnectionTimeout() {
        }

        @Override
        default public void close() {
        }
    }

    private static class SynchronousExecutor
    implements Executor {
        private SynchronousExecutor() {
        }

        @Override
        public void execute(Runnable command) {
            try {
                command.run();
            } catch (Exception t) {
                LoggerFactory.getLogger(PoolBase.class).debug("Failed to execute: {}", (Object)command, (Object)t);
            }
        }
    }

    static class ConnectionSetupException
    extends Exception {
        private static final long serialVersionUID = 929872118275916521L;

        ConnectionSetupException(Throwable t) {
            super(t);
        }
    }
}

