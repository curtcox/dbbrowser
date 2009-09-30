package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.stores.ActiveFunction;
import com.cve.stores.IO;
import com.cve.stores.IOs;
import com.cve.util.Check;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;

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

    private interface F {
        Object of(Object x) throws SQLException;
    }

    private Function of(String name, IO io, final F f) {
        return ActiveFunction.fileIOFunc(
            name, IOs.tableListToColumnList(), new Function() {
                @Override
                public Object apply(Object from) {
                    try {
                        return f.of(from);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );
    }

    private final Function<ImmutableList<DBTable>,ImmutableList<DBColumn>> primaryKeys =
        of(
            "primaryKeys", IOs.tableListToColumnList(), new F() {
                @Override
                public ImmutableList<DBColumn> of(Object from) throws SQLException {
                    return meta.getPrimaryKeysFor((ImmutableList<DBTable>)from);
                }
            }
        );

    @Override
    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        return primaryKeys.apply(tables);
    }

    private final Function<ImmutableList<DBTable>,ImmutableList<Join>> joins =
        of(
            "joins", IOs.tableListToJoinList(), new F() {
                @Override
                public ImmutableList<Join> of(Object from) throws SQLException {
                    return meta.getJoinsFor((ImmutableList<DBTable>) from);
                }
            }
        );
    @Override
    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        return joins.apply(tables);
    }

    private final Function<Server,ImmutableList<DBColumn>> columnsForServer =
        of(
            "columnsForServer", IOs.serverToColumnList(), new F() {
                @Override
                public ImmutableList<DBColumn> of(Object from) throws SQLException {
                    return meta.getColumnsFor((Server)from);
                }
            }
        );
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        return columnsForServer.apply(server);
    }

    private final Function<Database,ImmutableList<DBColumn>> columnsForDatabase =
        of(
            "columnsForDatabase", IOs.serverToColumnList(), new F() {
                @Override
                public ImmutableList<DBColumn> of(Object from) throws SQLException {
                    return meta.getColumnsFor((Database)from);
                }
            }
        );
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Database database) throws SQLException {
        return columnsForDatabase.apply(database);
    }

    private final Function<DBTable,ImmutableList<DBColumn>> columnsForTable =
        of(
            "columnsForTable", IOs.serverToColumnList(), new F() {
                @Override
                public ImmutableList<DBColumn> of(Object from) {
                    try {
                        return meta.getColumnsFor((DBTable)from);
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

    private final Function<Server,ImmutableList<Database>> databases =
        of(
            "databases", IOs.serverToDatabaseList(), new F() {
                @Override
                public ImmutableList<Database> of(Object from) {
                    try {
                        return meta.getDatabasesOn((Server)from);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );
    @Override
    public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        return databases.apply(server);
    }

    private final Function<ImmutableList<DBTable>,ImmutableList<DBColumn>> columnsForTables =
        of(
            "columnsForTables", IOs.tableListToColumnList(), new F() {
                @Override
                public ImmutableList<DBColumn> of(Object from) throws SQLException {
                    return meta.getColumnsFor((ImmutableList<DBTable>)from);
                }
            }
        );
    @Override
    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        return columnsForTables.apply(tables);
    }

    private final Function<Database,ImmutableList<DBTable>> tables =
        of(
            "tables", IOs.databaseToTableList(), new F() {
                @Override
                public ImmutableList<DBTable> of(Object from) throws SQLException {
                    return meta.getTablesOn((Database)from);
                }
            }
        );
    @Override
    public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        return tables.apply(database);
    }

    private final Function<DBTable,Long> rowCounts =
        of(
            "rowCounts", IOs.tableToLong(), new F() {
                @Override
                public Long of(Object from) throws SQLException {
                    return meta.getRowCountFor((DBTable)from);
                }
            }
        );
    @Override
    public long getRowCountFor(DBTable table) throws SQLException {
        return rowCounts.apply(table);
    }


}
