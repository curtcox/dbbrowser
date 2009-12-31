package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.stores.CurrentValue;
import com.google.common.collect.ImmutableList;
import java.io.PrintStream;
import java.sql.SQLException;

/**
 * For wrapping a DBMetaData and adding logging.
 * @author Curt
 */
public final class DBMetaDataLogger implements DBMetaData {

    final PrintStream out;
    final DBMetaData meta;

    private DBMetaDataLogger(PrintStream out, DBMetaData meta) {
        this.out = out;
        this.meta = meta;
    }

    public static DBMetaData of(PrintStream out, DBMetaData meta) {
        return new DBMetaDataLogger(out,meta);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        print("getPrimaryKeysFor" + tables);
        return print(meta.getPrimaryKeysFor(tables));
    }

    @Override
    public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        print("getJoinsFor" + tables);
        return print(meta.getJoinsFor(tables));
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Server server) throws SQLException {
        print("getColumnsFor" + server);
        return print(meta.getColumnsFor(server));
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) throws SQLException {
        print("getColumnsFor" + database);
        return print(meta.getColumnsFor(database));
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) throws SQLException {
        print("getColumnsFor" + table);
        return print(meta.getColumnsFor(table));
    }

    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(Server server) throws SQLException {
        print("getDatabasesOn" + server);
        return print(meta.getDatabasesOn(server));
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        print("getColumnsFor" + tables);
        return print(meta.getColumnsFor(tables));
    }

    @Override
    public CurrentValue<Long> getRowCountFor(DBTable table) throws SQLException {
        print("getRowCountFor" + table);
        return print(meta.getRowCountFor(table));
    }

    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) throws SQLException {
        print("getTablesOn" + database);
        return print(meta.getTablesOn(database));
    }

    <T> T print(T t) {
        out.println(t);
        return t;
    }

}
