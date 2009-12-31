package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.CurrentValue;
import com.cve.stores.MapIO;
import com.cve.stores.IOs;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.sql.SQLException;

/**
 * Wraps a DBMetaData to provide caching for it.
 * @author curt
 */
public final class DBMetaDataFunctions implements DBMetaData {

    private final DBMetaData meta;

    private final File baseDir;

    private DBMetaDataFunctions(DBMetaData meta, File baseDir) {
        this.meta = Check.notNull(meta);
        this.baseDir = Check.notNull(baseDir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        if (!baseDir.isDirectory()) {
            throw new RuntimeException(baseDir + " is not a directory.");
        }
        if (!baseDir.exists()) {
            throw new RuntimeException(baseDir + " could not be created.");
        }
    }

    public static DBMetaData of(DBMetaData meta, File baseDir) {
        return new DBMetaDataFunctions(meta,baseDir);
    }

    private ManagedFunction of(final String name, final MapIO io, final SQLFunction f) {
        final File file = new File(baseDir + File.separator + name);
        throw new UnsupportedOperationException();
    }

    private final ManagedFunction<ImmutableList<DBTable>,ImmutableList<DBColumn>> primaryKeys =
        of(
            "primaryKeys", IOs.tableListToColumnList(), new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) throws SQLException {
                    return meta.getPrimaryKeysFor((ImmutableList<DBTable>)from);
                }
            }
        );

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables) {
        try {
            return primaryKeys.apply(tables);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<ImmutableList<DBTable>,ImmutableList<Join>> joins =
        of(
            "joins", IOs.tableListToJoinList(), new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<Join>> apply(Object from) throws SQLException {
                    return meta.getJoinsFor((ImmutableList<DBTable>) from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        try {
            return joins.apply(tables);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<Server,ImmutableList<DBColumn>> columnsForServer =
        of(
            "columnsForServer", IOs.serverToColumnList(), new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((Server)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Server server) throws SQLException {
        try {
            return columnsForServer.apply(server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<Database,ImmutableList<DBColumn>> columnsForDatabase =
        of(
            "columnsForDatabase", IOs.serverToColumnList(), new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((Database)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) throws SQLException {
        try {
            return columnsForDatabase.apply(database);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<DBTable,ImmutableList<DBColumn>> columnsForTable =
        of(
            "columnsForTable", IOs.serverToColumnList(), new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((DBTable)from);
                }
            }
        );

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) throws SQLException {
        try {
            return columnsForTable.apply(table);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<Server,ImmutableList<Database>> databases =
        of(
            "databases", IOs.serverToDatabaseList(), new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<Database>> apply(Object from) throws SQLException {
                    return meta.getDatabasesOn((Server)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(Server server) throws SQLException {
        try {
            return databases.apply(server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<ImmutableList<DBTable>,ImmutableList<DBColumn>> columnsForTables =
        of(
            "columnsForTables", IOs.tableListToColumnList(), new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((ImmutableList<DBTable>)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        try {
            return columnsForTables.apply(tables);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<Database,ImmutableList<DBTable>> tables =
        of(
            "tables", IOs.databaseToTableList(), new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBTable>> apply(Object from) throws SQLException {
                    return meta.getTablesOn((Database)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) throws SQLException {
        try {
            return tables.apply(database);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<DBTable,Long> rowCounts =
        of(
            "rowCounts", IOs.tableToLong(), new SQLFunction() {
                @Override
                public CurrentValue<Long> apply(Object from) throws SQLException {
                    return meta.getRowCountFor((DBTable)from);
                }
            }
        );
    @Override
    public CurrentValue<Long> getRowCountFor(DBTable table) throws SQLException {
        try {
            return rowCounts.apply(table);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
