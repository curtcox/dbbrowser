package com.cve.io.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.Join;
import com.cve.model.db.DBServer;
import com.cve.stores.CurrentValue;
import com.cve.util.Check;
import com.cve.util.Stopwatch;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;

/**
 * For timing meta data access.
 */
public final class DBMetaDataTimer implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataTimer(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataTimer(meta);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables) {
        Stopwatch watch = Stopwatch.start(tables);
        CurrentValue<ImmutableList<DBColumn>> result = meta.getPrimaryKeysFor(tables);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) {
        Stopwatch watch = Stopwatch.start(tables);
        CurrentValue<ImmutableList<Join>> result = meta.getJoinsFor(tables);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server) {
        Stopwatch watch = Stopwatch.start(server);
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(server);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) {
        Stopwatch watch = Stopwatch.start(database);
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(database);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) {
        Stopwatch watch = Stopwatch.start(table);
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(table);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<Long> getRowCountFor(DBTable table) {
        Stopwatch watch = Stopwatch.start(table);
        CurrentValue<Long> result = meta.getRowCountFor(table);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server) {
        Stopwatch watch = Stopwatch.start(server);
        CurrentValue<ImmutableList<Database>> result = meta.getDatabasesOn(server);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) {
        Stopwatch watch = Stopwatch.start(tables);
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(tables);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) {
        Stopwatch watch = Stopwatch.start(database);
        CurrentValue<ImmutableList<DBTable>> result = meta.getTablesOn(database);
        watch.stop();
        return result;
    }

}
