package com.cve.io.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.Join;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.CurrentValue;
import com.cve.stores.IO;
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

    private ManagedFunction of(final String name, final IO io, final SQLFunction f) {
        final File file = new File(baseDir + File.separator + name);
        throw new UnsupportedOperationException();
    }

    private final ManagedFunction<ImmutableList<DBTable>,ImmutableList<DBColumn>> primaryKeys =
        of(
            "primaryKeys", null, new SQLFunction() {
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
            "joins", null, new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<Join>> apply(Object from) throws SQLException {
                    return meta.getJoinsFor((ImmutableList<DBTable>) from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables)  {
        try {
            return joins.apply(tables);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<DBServer,ImmutableList<DBColumn>> columnsForServer =
        of(
            "columnsForServer", null, new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) {
                    return meta.getColumnsFor((DBServer)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server) {
        try {
            return columnsForServer.apply(server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<Database,ImmutableList<DBColumn>> columnsForDatabase =
        of(
            "columnsForDatabase", null, new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((Database)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) {
        try {
            return columnsForDatabase.apply(database);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<DBTable,ImmutableList<DBColumn>> columnsForTable =
        of(
            "columnsForTable", null, new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((DBTable)from);
                }
            }
        );

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) {
        try {
            return columnsForTable.apply(table);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<DBServer,ImmutableList<Database>> databases =
        of(
            "databases", null, new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<Database>> apply(Object from) throws SQLException {
                    return meta.getDatabasesOn((DBServer)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server) {
        try {
            return databases.apply(server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<ImmutableList<DBTable>,ImmutableList<DBColumn>> columnsForTables =
        of(
            "columnsForTables", null, new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBColumn>> apply(Object from) throws SQLException {
                    return meta.getColumnsFor((ImmutableList<DBTable>)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) {
        try {
            return columnsForTables.apply(tables);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<Database,ImmutableList<DBTable>> tables =
        of(
            "tables", null, new SQLFunction() {
                @Override
                public CurrentValue<ImmutableList<DBTable>> apply(Object from) throws SQLException {
                    return meta.getTablesOn((Database)from);
                }
            }
        );
    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database)  {
        try {
            return tables.apply(database);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ManagedFunction<DBTable,Long> rowCounts =
        of(
            "rowCounts", null, new SQLFunction() {
                @Override
                public CurrentValue<Long> apply(Object from) throws SQLException {
                    return meta.getRowCountFor((DBTable)from);
                }
            }
        );
    @Override
    public CurrentValue<Long> getRowCountFor(DBTable table) {
        try {
            return rowCounts.apply(table);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
