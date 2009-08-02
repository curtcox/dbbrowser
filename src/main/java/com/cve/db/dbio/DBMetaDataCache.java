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
 *
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

    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        return meta.getPrimaryKeysFor(tables);
    }

    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables) throws SQLException {
        return meta.getJoinsFor(tables);
    }

    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        return meta.getColumnsFor(server);
    }

    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        return meta.getColumnsFor(table);
    }

    public ImmutableList<Database> getDatabasesOn(Server server) throws SQLException {
        return meta.getDatabasesOn(server);
    }

    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables) throws SQLException {
        return meta.getColumnsFor(tables);
    }

    public ImmutableList<DBTable> getTablesOn(Database database) throws SQLException {
        return meta.getTablesOn(database);
    }

}
