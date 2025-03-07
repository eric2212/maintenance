/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.hikari.pool;

import eu.kennytv.maintenance.lib.hikari.pool.HikariProxyCallableStatement;
import eu.kennytv.maintenance.lib.hikari.pool.HikariProxyConnection;
import eu.kennytv.maintenance.lib.hikari.pool.HikariProxyDatabaseMetaData;
import eu.kennytv.maintenance.lib.hikari.pool.HikariProxyPreparedStatement;
import eu.kennytv.maintenance.lib.hikari.pool.HikariProxyResultSet;
import eu.kennytv.maintenance.lib.hikari.pool.HikariProxyStatement;
import eu.kennytv.maintenance.lib.hikari.pool.PoolEntry;
import eu.kennytv.maintenance.lib.hikari.pool.ProxyConnection;
import eu.kennytv.maintenance.lib.hikari.pool.ProxyLeakTask;
import eu.kennytv.maintenance.lib.hikari.pool.ProxyStatement;
import eu.kennytv.maintenance.lib.hikari.util.FastList;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public final class ProxyFactory {
    private ProxyFactory() {
    }

    static ProxyConnection getProxyConnection(PoolEntry poolEntry, Connection connection, FastList<Statement> fastList, ProxyLeakTask proxyLeakTask, long l, boolean bl, boolean bl2) {
        return new HikariProxyConnection(poolEntry, connection, (FastList)fastList, proxyLeakTask, l, bl, bl2);
    }

    static Statement getProxyStatement(ProxyConnection proxyConnection, Statement statement) {
        return new HikariProxyStatement(proxyConnection, statement);
    }

    static CallableStatement getProxyCallableStatement(ProxyConnection proxyConnection, CallableStatement callableStatement) {
        return new HikariProxyCallableStatement(proxyConnection, callableStatement);
    }

    static PreparedStatement getProxyPreparedStatement(ProxyConnection proxyConnection, PreparedStatement preparedStatement) {
        return new HikariProxyPreparedStatement(proxyConnection, preparedStatement);
    }

    static ResultSet getProxyResultSet(ProxyConnection proxyConnection, ProxyStatement proxyStatement, ResultSet resultSet) {
        return new HikariProxyResultSet(proxyConnection, proxyStatement, resultSet);
    }

    static DatabaseMetaData getProxyDatabaseMetaData(ProxyConnection proxyConnection, DatabaseMetaData databaseMetaData) {
        return new HikariProxyDatabaseMetaData(proxyConnection, databaseMetaData);
    }
}

