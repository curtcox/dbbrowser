package com.cve.io.db;


import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.Join;
import com.cve.model.db.DBServer;
import com.cve.stores.CurrentValue;
import com.google.common.collect.ImmutableList;


/**
 * Database meta data interface.
 * This interface is exposed for use by the rest of the application.
 * It is primarily a simplified version of the standard Java interface.
 * It is needed as an abstraction layer, because
 * different drivers use differenent semantics.  The meaning and content of
 * "Catalog" and "Schema" columns are inconsistent.
 * <p>
 * Also important is that it enables asynchronous semantics through use of
 * CurrentValueS.
 * @author Curt
 */
public interface DBMetaData {

    /**
     * For making DBMetaDataS.
     */
    public interface Factory {
        DBMetaData of(DBServer server);
    }

    CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables);


    /**
     * Return all the potential joins from the columns in the given tables.
     */
    CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables);


    CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server);

    CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database);

    CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table);


    CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server);


    CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables);


    CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database);

    CurrentValue<Long> getRowCountFor(DBTable table);

}
