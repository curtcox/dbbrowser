package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.Map;

/**
 * Wraps a DBMetaData to provide caching for it.
 * @author curt
 */
final class DBMetaDataCache implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataCache(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataCache(meta);
    }

    private final Map<ImmutableList<DBTable>,ImmutableList<DBColumn>> primaryKeys = Maps.newHashMap();
    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        if (primaryKeys.containsKey(tables)) {
            return primaryKeys.get(tables);
        }
        ImmutableList<DBColumn> result = meta.getPrimaryKeysFor(tables);
        primaryKeys.put(tables, result);
        return result;
    }

    private final Map<ImmutableList<DBTable>,ImmutableList<Join>> joins = Maps.newHashMap();
    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        if (joins.containsKey(tables)) {
            return joins.get(tables);
        }
        ImmutableList<Join> result = meta.getJoinsFor(tables);
        joins.put(tables, result);
        return result;
    }

    private final Map<Server,ImmutableList<DBColumn>> columnsForServer = Maps.newHashMap();
    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        if (columnsForServer.containsKey(server)) {
            return columnsForServer.get(server);
        }
        ImmutableList<DBColumn> result = meta.getColumnsFor(server);
        columnsForServer.put(server, result);
        return result;
    }

    private final Map<DBTable,ImmutableList<DBColumn>> columnsForTable = Maps.newHashMap();
    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        if (columnsForTable.containsKey(table)) {
            return columnsForTable.get(table);
        }
        ImmutableList<DBColumn> result = meta.getColumnsFor(table);
        columnsForTable.put(table, result);
        return result;
    }

    private final Map<Server,ImmutableList<Database>> databases = Maps.newHashMap();
    public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        if (databases.containsKey(server)) {
            return databases.get(server);
        }
        ImmutableList<Database> result = meta.getDatabasesOn(server);
        databases.put(server, result);
        return result;
    }

    private final Map<ImmutableList<DBTable>,ImmutableList<DBColumn>> columnsForTables = Maps.newHashMap();
    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        if (columnsForTables.containsKey(tables)) {
            return columnsForTables.get(tables);
        }
        ImmutableList<DBColumn> result = meta.getColumnsFor(tables);
        columnsForTables.put(tables, result);
        return result;
    }

    private final Map<Database,ImmutableList<DBTable>> tables = Maps.newHashMap();
    public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        if (tables.containsKey(database)) {
            return tables.get(database);
        }
        ImmutableList<DBTable> result = meta.getTablesOn(database);
        tables.put(database, result);
        return result;
    }


}
