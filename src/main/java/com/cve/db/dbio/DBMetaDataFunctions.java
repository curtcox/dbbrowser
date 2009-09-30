package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.stores.ActiveFunction;
import com.cve.stores.IOs;
import com.cve.util.Check;
import com.cve.util.SimpleCache;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;
import java.util.Map;

/**
 * Wraps a DBMetaData to provide caching for it.
 * @author curt
 */
public final class DBMetaDataFunctions implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataFunctions(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataFunctions(meta);
    }

    private final Function<ImmutableList<DBTable>,ImmutableList<DBColumn>> primaryKeys =
        ActiveFunction.fileIOFunc(
            "primaryKeys", IOs.tableListToColumnList(), new Function<ImmutableList<DBTable>,ImmutableList<DBColumn>>() {
                @Override
                public ImmutableList<DBColumn> apply(ImmutableList<DBTable> from) {
                    try {
                        return meta.getPrimaryKeysFor(from);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );

    @Override
    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        return primaryKeys.apply(tables);
    }

    private final Function<ImmutableList<DBTable>,ImmutableList<Join>> joins =
        ActiveFunction.fileIOFunc(
            "joins", IOs.tableListToJoinList(), new Function<ImmutableList<DBTable>,ImmutableList<Join>>() {
                @Override
                public ImmutableList<Join> apply(ImmutableList<DBTable> from) {
                    try {
                        return meta.getJoinsFor(from);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );
    @Override
    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        return joins.apply(tables);
    }

    private final Function<Server,ImmutableList<DBColumn>> columnsForServer =
        ActiveFunction.fileIOFunc(
            "columnsForServer", IOs.serverToColumnList(), new Function<Server,ImmutableList<DBColumn>>() {
                @Override
                public ImmutableList<DBColumn> apply(Server from) {
                    try {
                        return meta.getColumnsFor(from);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        return columnsForServer.apply(server);
    }

    private final Function<Database,ImmutableList<DBColumn>> columnsForDatabase =
        ActiveFunction.fileIOFunc(
            "columnsForDatabase", IOs.serverToColumnList(), new Function<Database,ImmutableList<DBColumn>>() {
                @Override
                public ImmutableList<DBColumn> apply(Database from) {
                    try {
                        return meta.getColumnsFor(from);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Database database) throws SQLException {
        return columnsForDatabase.apply(database);
    }

    private final Function<DBTable,ImmutableList<DBColumn>> columnsForTable =
        ActiveFunction.fileIOFunc(
            "columnsForTable", IOs.serverToColumnList(), new Function<DBTable,ImmutableList<DBColumn>>() {
                @Override
                public ImmutableList<DBColumn> apply(DBTable from) {
                    try {
                        return meta.getColumnsFor(from);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );

    @Override
    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        return columnsForTable.apply(table);
    }

    private final Map<Server,ImmutableList<Database>> databases = SimpleCache.of();
    @Override
    public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        if (databases.containsKey(server)) {
            return databases.get(server);
        }
        ImmutableList<Database> result = meta.getDatabasesOn(server);
        databases.put(server, result);
        return result;
    }

    private final Map<ImmutableList<DBTable>,ImmutableList<DBColumn>> columnsForTables = SimpleCache.of();
    @Override
    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        if (columnsForTables.containsKey(tables)) {
            return columnsForTables.get(tables);
        }
        ImmutableList<DBColumn> result = meta.getColumnsFor(tables);
        columnsForTables.put(tables, result);
        return result;
    }

    private final Map<Database,ImmutableList<DBTable>> tables = SimpleCache.of();
    @Override
    public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        if (tables.containsKey(database)) {
            return tables.get(database);
        }
        ImmutableList<DBTable> result = meta.getTablesOn(database);
        tables.put(database, result);
        return result;
    }

    private final Map<DBTable,Long> rowCounts = SimpleCache.of();
    @Override
    public long getRowCountFor(DBTable table) throws SQLException {
        if (rowCounts.containsKey(table)) {
            return rowCounts.get(table);
        }
        Long result = meta.getRowCountFor(table);
        rowCounts.put(table, result);
        return result;
    }


}
