package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.stores.CurrentResult;
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

    private final Map<ImmutableList<DBTable>,CurrentResult<ImmutableList<DBColumn>>> primaryKeys = SimpleCache.of();
    @Override
    public CurrentResult<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        if (primaryKeys.containsKey(tables)) {
            return primaryKeys.get(tables);
        }
        CurrentResult<ImmutableList<DBColumn>> result = meta.getPrimaryKeysFor(tables);
        primaryKeys.put(tables, result);
        return result;
    }

    private final Map<ImmutableList<DBTable>,CurrentResult<ImmutableList<Join>>> joins = SimpleCache.of();
    @Override
    public CurrentResult<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        if (joins.containsKey(tables)) {
            return joins.get(tables);
        }
        CurrentResult<ImmutableList<Join>> result = meta.getJoinsFor(tables);
        joins.put(tables, result);
        return result;
    }

    private final Map<Server,CurrentResult<ImmutableList<DBColumn>>> columnsForServer = SimpleCache.of();
    @Override
    public CurrentResult<ImmutableList<DBColumn>> getColumnsFor(Server server) throws SQLException {
        if (columnsForServer.containsKey(server)) {
            return columnsForServer.get(server);
        }
        CurrentResult<ImmutableList<DBColumn>> result = meta.getColumnsFor(server);
        columnsForServer.put(server, result);
        return result;
    }

    private final Map<Database,CurrentResult<ImmutableList<DBColumn>>> columnsForDatabase = SimpleCache.of();
    @Override
    public CurrentResult<ImmutableList<DBColumn>> getColumnsFor(Database database) throws SQLException {
        if (columnsForDatabase.containsKey(database)) {
            return columnsForDatabase.get(database);
        }
        CurrentResult<ImmutableList<DBColumn>> result = meta.getColumnsFor(database);
        columnsForDatabase.put(database, result);
        return result;
    }

    private final Map<DBTable,CurrentResult<ImmutableList<DBColumn>>> columnsForTable = SimpleCache.of();
    @Override
    public CurrentResult<ImmutableList<DBColumn>> getColumnsFor(DBTable table) throws SQLException {
        if (columnsForTable.containsKey(table)) {
            return columnsForTable.get(table);
        }
        CurrentResult<ImmutableList<DBColumn>> result = meta.getColumnsFor(table);
        columnsForTable.put(table, result);
        return result;
    }

    private final Map<Server,CurrentResult<ImmutableList<Database>>> databases = SimpleCache.of();
    @Override
    public CurrentResult<ImmutableList<Database>> getDatabasesOn(Server server) throws SQLException {
        if (databases.containsKey(server)) {
            return databases.get(server);
        }
        CurrentResult<ImmutableList<Database>> result = meta.getDatabasesOn(server);
        databases.put(server, result);
        return result;
    }

    private final Map<ImmutableList<DBTable>,CurrentResult<ImmutableList<DBColumn>>> columnsForTables = SimpleCache.of();
    @Override
    public CurrentResult<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        if (columnsForTables.containsKey(tables)) {
            return columnsForTables.get(tables);
        }
        CurrentResult<ImmutableList<DBColumn>> result = meta.getColumnsFor(tables);
        columnsForTables.put(tables, result);
        return result;
    }

    private final Map<Database,CurrentResult<ImmutableList<DBTable>>> tables = SimpleCache.of();
    @Override
    public CurrentResult<ImmutableList<DBTable>> getTablesOn(Database database) throws SQLException {
        if (tables.containsKey(database)) {
            return tables.get(database);
        }
        CurrentResult<ImmutableList<DBTable>> result = meta.getTablesOn(database);
        tables.put(database, result);
        return result;
    }

    private final Map<DBTable,CurrentResult<Long>> rowCounts = SimpleCache.of();
    @Override
    public CurrentResult<Long> getRowCountFor(DBTable table) throws SQLException {
        if (rowCounts.containsKey(table)) {
            return rowCounts.get(table);
        }
        CurrentResult<Long> result = meta.getRowCountFor(table);
        rowCounts.put(table, result);
        return result;
    }


}
