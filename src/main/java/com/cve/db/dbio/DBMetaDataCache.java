package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.DBServer;
import com.cve.stores.CurrentValue;
import com.cve.util.Check;
import com.cve.util.SimpleCache;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;
import java.util.Map;

/**
 * Wraps a DBMetaData to provide caching for it.
 * @author curt
 */
public final class DBMetaDataCache implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataCache(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataCache(meta);
    }

    private final Map<ImmutableList<DBTable>,CurrentValue<ImmutableList<DBColumn>>> primaryKeys = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables)  {
        if (primaryKeys.containsKey(tables)) {
            return primaryKeys.get(tables);
        }
        CurrentValue<ImmutableList<DBColumn>> result = meta.getPrimaryKeysFor(tables);
        primaryKeys.put(tables, result);
        return result;
    }

    private final Map<ImmutableList<DBTable>,CurrentValue<ImmutableList<Join>>> joins = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) {
        if (joins.containsKey(tables)) {
            return joins.get(tables);
        }
        CurrentValue<ImmutableList<Join>> result = meta.getJoinsFor(tables);
        joins.put(tables, result);
        return result;
    }

    private final Map<DBServer,CurrentValue<ImmutableList<DBColumn>>> columnsForServer = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server) {
        if (columnsForServer.containsKey(server)) {
            return columnsForServer.get(server);
        }
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(server);
        columnsForServer.put(server, result);
        return result;
    }

    private final Map<Database,CurrentValue<ImmutableList<DBColumn>>> columnsForDatabase = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) {
        if (columnsForDatabase.containsKey(database)) {
            return columnsForDatabase.get(database);
        }
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(database);
        columnsForDatabase.put(database, result);
        return result;
    }

    private final Map<DBTable,CurrentValue<ImmutableList<DBColumn>>> columnsForTable = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) {
        if (columnsForTable.containsKey(table)) {
            return columnsForTable.get(table);
        }
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(table);
        columnsForTable.put(table, result);
        return result;
    }

    private final Map<DBServer,CurrentValue<ImmutableList<Database>>> databases = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server) {
        if (databases.containsKey(server)) {
            return databases.get(server);
        }
        CurrentValue<ImmutableList<Database>> result = meta.getDatabasesOn(server);
        databases.put(server, result);
        return result;
    }

    private final Map<ImmutableList<DBTable>,CurrentValue<ImmutableList<DBColumn>>> columnsForTables = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) {
        if (columnsForTables.containsKey(tables)) {
            return columnsForTables.get(tables);
        }
        CurrentValue<ImmutableList<DBColumn>> result = meta.getColumnsFor(tables);
        columnsForTables.put(tables, result);
        return result;
    }

    private final Map<Database,CurrentValue<ImmutableList<DBTable>>> tables = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) {
        if (tables.containsKey(database)) {
            return tables.get(database);
        }
        CurrentValue<ImmutableList<DBTable>> result = meta.getTablesOn(database);
        tables.put(database, result);
        return result;
    }

    private final Map<DBTable,CurrentValue<Long>> rowCounts = SimpleCache.of();
    @Override
    public CurrentValue<Long> getRowCountFor(DBTable table) {
        if (rowCounts.containsKey(table)) {
            return rowCounts.get(table);
        }
        CurrentValue<Long> result = meta.getRowCountFor(table);
        rowCounts.put(table, result);
        return result;
    }


}
