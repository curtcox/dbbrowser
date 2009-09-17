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
 * Wraps another meta to provide thread safety.
 * Right now, we just synchronize, but we could do something better if needed.
 * @author curt
 */
public final class DBMetaDataLocked implements DBMetaData {

    private final DBMetaData meta;

    private DBMetaDataLocked(DBMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DBMetaData of(DBMetaData meta) {
        return new DBMetaDataLocked(meta);
    }

    @Override
    synchronized public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        return meta.getPrimaryKeysFor(tables);
    }

    @Override
    synchronized public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        return meta.getJoinsFor(tables);
    }

    @Override
    synchronized public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        return meta.getColumnsFor(server);
    }

    @Override
    synchronized public ImmutableList<DBColumn> getColumnsFor(Database database) throws SQLException {
        return meta.getColumnsFor(database);
    }

    @Override
    synchronized public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        return meta.getColumnsFor(table);
    }

    @Override
    synchronized public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        return meta.getDatabasesOn(server);
    }

    @Override
    synchronized public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        return meta.getColumnsFor(tables);
    }

    @Override
    synchronized public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        return meta.getTablesOn(database);
    }

    @Override
    synchronized public long getRowCountFor(DBTable table) throws SQLException {
        return meta.getRowCountFor(table);
    }

}
