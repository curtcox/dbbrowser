package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;

/**
 * Why would you need such a horrible thing?
 * This might well be as wrong as it sounds.
 * Unfortunately, some JDBC drivers will throw security exceptions, rather
 * than just limiting the data they return.
 */
public final class DBMetaDataExceptionEater implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataExceptionEater(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataExceptionEater(meta);
    }

    @Override
    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) {
        try {
            return meta.getPrimaryKeysFor(tables);
        } catch (SQLException e) {
            report(e);
            return ImmutableList.of();
        }
    }

    @Override
    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) {
        try {
            return meta.getJoinsFor(tables);
        } catch (SQLException e) {
            report(e);
            return ImmutableList.of();
        }
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(Server server) {
        try {
            return meta.getColumnsFor(server);
        } catch (SQLException e) {
            report(e);
            return ImmutableList.of();
        }
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(Database database) {
        try {
            return meta.getColumnsFor(database);
        } catch (SQLException e) {
            report(e);
            return ImmutableList.of();
        }
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(DBTable table) {
        try {
            return meta.getColumnsFor(table);
        } catch (SQLException e) {
            report(e);
            return ImmutableList.of();
        }
    }

    @Override
    public ImmutableList<Database> getDatabasesOn(Server server) {
        try {
            return meta.getDatabasesOn(server);
        } catch (SQLException e) {
            report(e);
            return ImmutableList.of();
        }
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) {
        try {
            return meta.getColumnsFor(tables);
        } catch (SQLException e) {
            report(e);
            return ImmutableList.of();
        }
    }

    @Override
    public ImmutableList<DBTable> getTablesOn(Database database) {
        try {
            return meta.getTablesOn(database);
        } catch (SQLException e) {
            report(e);
            return ImmutableList.of();
        }
    }

        @Override
    public long getRowCountFor(DBTable table) {
        try {
            return meta.getRowCountFor(table);
        } catch (SQLException e) {
            report(e);
            return 0;
        }
    }

    private static void report(SQLException e) {
        // this should consolidate and log
    }

}
