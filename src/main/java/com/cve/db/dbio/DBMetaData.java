package com.cve.db.dbio;


import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
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

    ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables)  throws SQLException;


    /**
     * Return all the potential joins from the columns in the given tables.
     */
    ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables)  throws SQLException;


    ImmutableList<DBColumn> getColumnsFor(Server server)  throws SQLException;

    ImmutableList<DBColumn> getColumnsFor(Database database)  throws SQLException;

    ImmutableList<DBColumn> getColumnsFor(DBTable table)  throws SQLException;


    ImmutableList<Database> getDatabasesOn(Server server)  throws SQLException;


    ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables)  throws SQLException;


    ImmutableList<DBTable> getTablesOn(Database database)  throws SQLException;

    long getRowCountFor(DBTable table) throws SQLException;

}
