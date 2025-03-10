/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.hikari.pool;

import eu.kennytv.maintenance.lib.hikari.pool.ProxyConnection;
import eu.kennytv.maintenance.lib.hikari.pool.ProxyFactory;
import eu.kennytv.maintenance.lib.hikari.pool.ProxyResultSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ProxyStatement
implements Statement {
    protected final ProxyConnection connection;
    final Statement delegate;
    private boolean isClosed;
    private ResultSet proxyResultSet;

    ProxyStatement(ProxyConnection connection, Statement statement) {
        this.connection = connection;
        this.delegate = statement;
    }

    final SQLException checkException(SQLException e) {
        return this.connection.checkException(e);
    }

    public final String toString() {
        String delegateToString = this.delegate.toString();
        return this.getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + delegateToString;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void close() throws SQLException {
        ProxyStatement proxyStatement = this;
        synchronized (proxyStatement) {
            if (this.isClosed) {
                return;
            }
            this.isClosed = true;
        }
        this.connection.untrackStatement(this.delegate);
        try {
            this.delegate.close();
        } catch (SQLException e) {
            throw this.connection.checkException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.execute(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.execute(sql, autoGeneratedKeys);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        this.connection.markCommitStateDirty();
        ResultSet resultSet = this.delegate.executeQuery(sql);
        return ProxyFactory.getProxyResultSet(this.connection, this, resultSet);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeUpdate(sql);
    }

    @Override
    public int[] executeBatch() throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeBatch();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.execute(sql, columnNames);
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeLargeBatch();
    }

    @Override
    public long executeLargeUpdate(String sql) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeLargeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeLargeUpdate(sql, columnIndexes);
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        this.connection.markCommitStateDirty();
        return this.delegate.executeLargeUpdate(sql, columnNames);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        ResultSet resultSet = this.delegate.getResultSet();
        if (resultSet != null) {
            if (this.proxyResultSet == null || ((ProxyResultSet)this.proxyResultSet).delegate != resultSet) {
                this.proxyResultSet = ProxyFactory.getProxyResultSet(this.connection, this, resultSet);
            }
        } else {
            this.proxyResultSet = null;
        }
        return this.proxyResultSet;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        ResultSet resultSet = this.delegate.getGeneratedKeys();
        if (this.proxyResultSet == null || ((ProxyResultSet)this.proxyResultSet).delegate != resultSet) {
            this.proxyResultSet = ProxyFactory.getProxyResultSet(this.connection, this, resultSet);
        }
        return this.proxyResultSet;
    }

    @Override
    public final <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this.delegate)) {
            return (T)this.delegate;
        }
        if (this.delegate != null) {
            return this.delegate.unwrap(iface);
        }
        throw new SQLException("Wrapped statement is not an instance of " + iface);
    }
}

