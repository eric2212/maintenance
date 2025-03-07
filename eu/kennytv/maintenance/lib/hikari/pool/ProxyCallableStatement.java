/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.hikari.pool;

import eu.kennytv.maintenance.lib.hikari.pool.ProxyConnection;
import eu.kennytv.maintenance.lib.hikari.pool.ProxyPreparedStatement;
import java.sql.CallableStatement;

public abstract class ProxyCallableStatement
extends ProxyPreparedStatement
implements CallableStatement {
    protected ProxyCallableStatement(ProxyConnection connection, CallableStatement statement) {
        super(connection, statement);
    }
}

