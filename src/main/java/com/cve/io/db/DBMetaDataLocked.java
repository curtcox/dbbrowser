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

/**
 * Wraps another meta to provide thread safety.
 * Right now, we just synchronize, but we could do something better if needed.
 * @author curt
 */
public final class DBMetaDataLocked implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataLocked(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataLocked(meta);
    }

    @Override
    synchronized public CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables) {
        return meta.getPrimaryKeysFor(tables);
    }

    @Override
    synchronized public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) {
        return meta.getJoinsFor(tables);
    }

    @Override
    synchronized public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server)  {
        return meta.getColumnsFor(server);
    }

    @Override
    synchronized public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) {
        return meta.getColumnsFor(database);
    }

    @Override
    synchronized public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) {
        return meta.getColumnsFor(table);
    }

    @Override
    synchronized public CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server) {
        return meta.getDatabasesOn(server);
    }

    @Override
    synchronized public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) {
        return meta.getColumnsFor(tables);
    }

    @Override
    synchronized public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) {
        return meta.getTablesOn(database);
    }

    @Override
    synchronized public CurrentValue<Long> getRowCountFor(DBTable table) {
        return meta.getRowCountFor(table);
    }

}
