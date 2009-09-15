package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.util.Check;
import com.cve.util.Stopwatch;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;

/**
 * For timing meta data access.
 */
final class DBMetaDataTimer implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataTimer(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataTimer(meta);
    }

    @Override
    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        Stopwatch watch = Stopwatch.start(tables);
        ImmutableList<DBColumn> result = meta.getPrimaryKeysFor(tables);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        Stopwatch watch = Stopwatch.start(tables);
        ImmutableList<Join> result = meta.getJoinsFor(tables);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        Stopwatch watch = Stopwatch.start(server);
        ImmutableList<DBColumn> result = meta.getColumnsFor(server);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(Database database) throws SQLException {
        Stopwatch watch = Stopwatch.start(database);
        ImmutableList<DBColumn> result = meta.getColumnsFor(database);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        Stopwatch watch = Stopwatch.start(table);
        ImmutableList<DBColumn> result = meta.getColumnsFor(table);
        watch.stop();
        return result;
    }

    @Override
    public long getRowCountFor(DBTable table) throws SQLException {
        Stopwatch watch = Stopwatch.start(table);
        long result = meta.getRowCountFor(table);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        Stopwatch watch = Stopwatch.start(server);
        ImmutableList<Database> result = meta.getDatabasesOn(server);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        Stopwatch watch = Stopwatch.start(tables);
        ImmutableList<DBColumn> result = meta.getColumnsFor(tables);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        Stopwatch watch = Stopwatch.start(database);
        ImmutableList<DBTable> result = meta.getTablesOn(database);
        watch.stop();
        return result;
    }

}
