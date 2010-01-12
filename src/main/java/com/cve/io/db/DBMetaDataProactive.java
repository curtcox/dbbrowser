package com.cve.io.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.Join;
import com.cve.model.db.DBServer;
import com.cve.stores.CurrentValue;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Wraps a DBMetaData to provide speculative querying of the underlying meta.
 * <p>
 * <b> Warning : </b> this should not be used without connection pooling,
 * and probably not even then.  The problem is that some of these calls can
 * take a really long time (Duration = 1,668,991ms = 27 minutes! for instance).
 * Without connection pooling, the database connection is needlessly blocked
 * for the entire period.
 * <p>
 * Even with connection pooling, a better approach is probably to provide drivers
 * that use bulk APIs under the covers.  That's probably harder to code, but
 * should give better results.
 * @author curt
 */
public final class DBMetaDataProactive implements DBMetaData {

    private final DBMetaData meta;

    private final Executor executor = Executors.newSingleThreadExecutor();

    private DBMetaDataProactive(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataProactive(meta);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables) {
        CurrentValue<ImmutableList<DBColumn>> result = meta.getPrimaryKeysFor(tables);
        queueColumns(result);
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) {
        CurrentValue<ImmutableList<Join>> result = meta.getJoinsFor(tables);
        queueJoins(result);
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server) {
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(server);
        queueColumns(result);
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) {
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(database);
        queueColumns(result);
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table)  {
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(table);
        queueColumns(result);
        return result;
    }

    @Override
    public CurrentValue<Long> getRowCountFor(DBTable table) {
        return meta.getRowCountFor(table);
    }

    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server) {
        CurrentValue<ImmutableList<Database>> result = meta.getDatabasesOn(server);
        queueServer(server);
        queueDatabases(result);
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) {
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(tables);
        queueColumns(result);
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) {
        CurrentValue<ImmutableList<DBTable>> result = meta.getTablesOn(database);
        queueTables(result);
        return result;
    }

    void queueTables(final CurrentValue<ImmutableList<DBTable>> tables) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                meta.getColumnsFor(tables.value);
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                meta.getJoinsFor(tables.value);
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                meta.getPrimaryKeysFor(tables.value);
            }
        });
    }

    void queueDatabases(CurrentValue<ImmutableList<Database>> databases) {
        for (final Database database : databases.value) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    meta.getTablesOn(database);
                }
            });
        }
    }

    void queueServer(final DBServer server) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                meta.getColumnsFor(server);
            }
        });
    }

    void queueColumns(CurrentValue<ImmutableList<DBColumn>> columns) {
        // nothing to do
    }

    void queueJoins(CurrentValue<ImmutableList<Join>> join) {
        // nothing to do
    }


}
