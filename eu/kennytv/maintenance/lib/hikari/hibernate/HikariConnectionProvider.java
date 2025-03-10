/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.hibernate.HibernateException
 *  org.hibernate.Version
 *  org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
 *  org.hibernate.service.UnknownUnwrapTypeException
 *  org.hibernate.service.spi.Configurable
 *  org.hibernate.service.spi.Stoppable
 */
package eu.kennytv.maintenance.lib.hikari.hibernate;

import eu.kennytv.maintenance.lib.hikari.HikariConfig;
import eu.kennytv.maintenance.lib.hikari.HikariDataSource;
import eu.kennytv.maintenance.lib.hikari.hibernate.HikariConfigurationUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.HibernateException;
import org.hibernate.Version;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HikariConnectionProvider
implements ConnectionProvider,
Configurable,
Stoppable {
    private static final long serialVersionUID = -9131625057941275711L;
    private static final Logger LOGGER = LoggerFactory.getLogger(HikariConnectionProvider.class);
    private HikariConfig hcfg = null;
    private HikariDataSource hds = null;

    public HikariConnectionProvider() {
        if (Version.getVersionString().substring(0, 5).compareTo("4.3.6") >= 1) {
            LOGGER.warn("eu.kennytv.maintenance.lib.hikari.hibernate.HikariConnectionProvider has been deprecated for versions of Hibernate 4.3.6 and newer.  Please switch to org.hibernate.hikaricp.internal.HikariCPConnectionProvider.");
        }
    }

    public void configure(Map props) throws HibernateException {
        try {
            LOGGER.debug("Configuring HikariCP");
            this.hcfg = HikariConfigurationUtil.loadConfiguration(props);
            this.hds = new HikariDataSource(this.hcfg);
        } catch (Exception e) {
            throw new HibernateException((Throwable)e);
        }
        LOGGER.debug("HikariCP Configured");
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        if (this.hds != null) {
            conn = this.hds.getConnection();
        }
        return conn;
    }

    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    public boolean supportsAggressiveRelease() {
        return false;
    }

    public boolean isUnwrappableAs(Class unwrapType) {
        return ConnectionProvider.class.equals((Object)unwrapType) || HikariConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    public <T> T unwrap(Class<T> unwrapType) {
        if (ConnectionProvider.class.equals(unwrapType) || HikariConnectionProvider.class.isAssignableFrom(unwrapType)) {
            return (T)this;
        }
        if (DataSource.class.isAssignableFrom(unwrapType)) {
            return (T)this.hds;
        }
        throw new UnknownUnwrapTypeException(unwrapType);
    }

    public void stop() {
        this.hds.close();
    }
}

