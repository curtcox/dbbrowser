package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.google.common.collect.ImmutableList;
import java.io.PrintStream;
import java.sql.SQLException;

/**
 *
 * @author Curt
 */
final class DBMetaDataLogger implements DBMetaData {

    final PrintStream out;
    final DBMetaData meta;

    private DBMetaDataLogger(PrintStream out, DBMetaData meta) {
        this.out = out;
        this.meta = meta;
    }

    static DBMetaData of(PrintStream out, DBMetaData meta) {
        return new DBMetaDataLogger(out,meta);
    }

    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        return print(meta.getPrimaryKeysFor(tables));
    }

    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        return print(meta.getJoinsFor(tables));
    }

    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        return print(meta.getColumnsFor(server));
    }

    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        return print(meta.getColumnsFor(table));
    }

    public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        return print(meta.getDatabasesOn(server));
    }

    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        return print(meta.getColumnsFor(tables));
    }

    public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        return print(meta.getTablesOn(database));
    }

    public <T> T print(T t) {
        out.println(t);
        return t;
    }
}
