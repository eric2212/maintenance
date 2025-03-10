/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.hikari.util;

public enum IsolationLevel {
    TRANSACTION_NONE(0),
    TRANSACTION_READ_UNCOMMITTED(1),
    TRANSACTION_READ_COMMITTED(2),
    TRANSACTION_REPEATABLE_READ(4),
    TRANSACTION_SERIALIZABLE(8),
    TRANSACTION_SQL_SERVER_SNAPSHOT_ISOLATION_LEVEL(4096);

    private final int levelId;

    private IsolationLevel(int levelId) {
        this.levelId = levelId;
    }

    public int getLevelId() {
        return this.levelId;
    }
}

