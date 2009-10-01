package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.stores.ActiveFunction;
import com.cve.stores.IO;
import com.cve.stores.IOs;
import com.cve.stores.SQLFunction;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
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

    private SQLFunction of(String name, IO io, final SQLFunction f) {
        File file = new File(baseDir + File.separator + name);
        try {
            return ActiveFunction.fileIOFunc(file, io, f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final SQLFunction<ImmutableList<DBTable>,ImmutableList<DBColumn>> primaryKeys =
        of(
            "primaryKeys", IOs.tableListToColumnList(), new SQLFunction() {
                @Override
                public ImmutableList<DBColumn> apply(Object from) throws SQLException {
                    return meta.getPrimaryKeysFor((ImmutableList<DBTable>)from);
                }
            }
        );

    @Override
    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        return primaryKeys.apply(tables);
    }

    private final SQLFunction<ImmutableList<DBTable>,ImmutableList<Join>> joins =
        of(
            "joins", IOs.tableListToJoinList(), new SQLFunction() {
                @Override
                public ImmutableList<Join> apply(Object from) throws SQLException {
                    return meta.getJoinsFor((ImmutableList<DBTable>) from);
                }
            }
        );
    @Override
    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        return joins.apply(tables);
    }

    private final SQLFunction<Server,ImmutableList<DBColumn>> columnsForServer =
        of(
            "columnsForServer", IOs.serverToColumnList(), new SQLFunction() {
                @Override
                public ImmutableList<DBColumn> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((Server)from);
                }
            }
        );
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        return columnsForServer.apply(server);
    }

    private final SQLFunction<Database,ImmutableList<DBColumn>> columnsForDatabase =
        of(
            "columnsForDatabase", IOs.serverToColumnList(), new SQLFunction() {
                @Override
                public ImmutableList<DBColumn> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((Database)from);
                }
            }
        );
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Database database) throws SQLException {
        return columnsForDatabase.apply(database);
    }

    private final SQLFunction<DBTable,ImmutableList<DBColumn>> columnsForTable =
        of(
            "columnsForTable", IOs.serverToColumnList(), new SQLFunction() {
                @Override
                public ImmutableList<DBColumn> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((DBTable)from);
                }
            }
        );

    @Override
    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        return columnsForTable.apply(table);
    }

    private final SQLFunction<Server,ImmutableList<Database>> databases =
        of(
            "databases", IOs.serverToDatabaseList(), new SQLFunction() {
                @Override
                public ImmutableList<Database> apply(Object from) throws SQLException {
                    return meta.getDatabasesOn((Server)from);
                }
            }
        );
    @Override
    public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        return databases.apply(server);
    }

    private final SQLFunction<ImmutableList<DBTable>,ImmutableList<DBColumn>> columnsForTables =
        of(
            "columnsForTables", IOs.tableListToColumnList(), new SQLFunction() {
                @Override
                public ImmutableList<DBColumn> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((ImmutableList<DBTable>)from);
                }
            }
        );
    @Override
    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        return columnsForTables.apply(tables);
    }

    private final SQLFunction<Database,ImmutableList<DBTable>> tables =
        of(
            "tables", IOs.databaseToTableList(), new SQLFunction() {
                @Override
                public ImmutableList<DBTable> apply(Object from) throws SQLException {
                    return meta.getTablesOn((Database)from);
                }
            }
        );
    @Override
    public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        return tables.apply(database);
    }

    private final SQLFunction<DBTable,Long> rowCounts =
        of(
            "rowCounts", IOs.tableToLong(), new SQLFunction() {
                @Override
                public Long apply(Object from) throws SQLException {
                    return meta.getRowCountFor((DBTable)from);
                }
            }
        );
    @Override
    public long getRowCountFor(DBTable table) throws SQLException {
        return rowCounts.apply(table);
    }


}
