package com.cve.db.dbio;


import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.stores.CurrentResult;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;


/**
 * Database meta data interface.
 * This interface is exposed for use by the rest of the application.
 * It is primarily a simplified version of the standard Java interface.
 * It is needed as an abstraction layer, because
 * different drivers use differenent semantics.  The meaning and content of
 * "Catalog" and "Schema" columns are inconsistent.
 * @author Curt
 */
public interface DBMetaData {

    /**
     * For making DBMetaDataS.
     */
    public interface Factory {
        DBMetaData of(Server server);
    }

    CurrentResult<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables)  throws SQLException;


    /**
     * Return all the potential joins from the columns in the given tables.
     */
    CurrentResult<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables)  throws SQLException;


    CurrentResult<ImmutableList<DBColumn>> getColumnsFor(Server server)  throws SQLException;

    CurrentResult<ImmutableList<DBColumn>> getColumnsFor(Database database)  throws SQLException;

    CurrentResult<ImmutableList<DBColumn>> getColumnsFor(DBTable table)  throws SQLException;


    CurrentResult<ImmutableList<Database>> getDatabasesOn(Server server)  throws SQLException;


    CurrentResult<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables)  throws SQLException;


    CurrentResult<ImmutableList<DBTable>> getTablesOn(Database database)  throws SQLException;

    CurrentResult<Long> getRowCountFor(DBTable table) throws SQLException;

}
