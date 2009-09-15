package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
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
final class DBMetaDataProactive implements DBMetaData {

    private final DBMetaData meta;

    private final Executor executor = Executors.newSingleThreadExecutor();

    private DBMetaDataProactive(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataProactive(meta);
    }

    @Override
    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        ImmutableList<DBColumn> result = meta.getPrimaryKeysFor(tables);
        queueColumns(result);
        return result;
    }

    @Override
    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        ImmutableList<Join> result = meta.getJoinsFor(tables);
        queueJoins(result);
        return result;
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        ImmutableList<DBColumn> result = meta.getColumnsFor(server);
        queueColumns(result);
        return result;
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(Database database) throws SQLException {
        ImmutableList<DBColumn> result = meta.getColumnsFor(database);
        queueColumns(result);
        return result;
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        ImmutableList<DBColumn> result = meta.getColumnsFor(table);
        queueColumns(result);
        return result;
    }

    @Override
    public long getRowCountFor(DBTable table) throws SQLException {
        return meta.getRowCountFor(table);
    }

    @Override
    public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        ImmutableList<Database> result = meta.getDatabasesOn(server);
        queueServer(server);
        queueDatabases(result);
        return result;
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        ImmutableList<DBColumn> result = meta.getColumnsFor(tables);
        queueColumns(result);
        return result;
    }

    @Override
    public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        ImmutableList<DBTable> result = meta.getTablesOn(database);
        queueTables(result);
        return result;
    }

    void queueTables(final ImmutableList<DBTable> tables) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    meta.getColumnsFor(tables);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    meta.getJoinsFor(tables);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    meta.getPrimaryKeysFor(tables);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void queueDatabases(ImmutableList<Database> databases) {
        for (final Database database : databases) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        meta.getTablesOn(database);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    void queueServer(final Server server) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    meta.getColumnsFor(server);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void queueColumns(ImmutableList<DBColumn> columns) {
        // nothing to do
    }

    void queueJoins(ImmutableList<Join> join) {
        // nothing to do
    }


}
