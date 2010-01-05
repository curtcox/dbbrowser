package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.DBServer;
import com.cve.stores.CurrentValue;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;

/**
 * Why would you need such a horrible thing?
 * This might well be as wrong as it sounds.
 * Unfortunately, some JDBC drivers will throw security exceptions, rather
 * than just limiting the data they return.
 */
public final class DBMetaDataExceptionEater implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataExceptionEater(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataExceptionEater(meta);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables) {
        return meta.getPrimaryKeysFor(tables);
    }

    @Override
    public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) {
        return meta.getJoinsFor(tables);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server) {
        return meta.getColumnsFor(server);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) {
        return meta.getColumnsFor(database);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) {
        return meta.getColumnsFor(table);
    }

    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server) {
        return meta.getDatabasesOn(server);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) {
        return meta.getColumnsFor(tables);
    }

    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) {
        return meta.getTablesOn(database);
    }

    @Override
    public CurrentValue<Long> getRowCountFor(DBTable table) {
        return meta.getRowCountFor(table);
    }

    private static void report(SQLException e) {
        // this should consolidate and log
    }

}
