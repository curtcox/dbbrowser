package com.cve.io.db.driver.h2;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBMetaDataIO;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBMetaDataIO.ColumnInfo;
import com.cve.io.db.DBMetaDataIO.ColumnSpecifier;
import com.cve.io.db.DBMetaDataIO.SchemaInfo;
import com.cve.io.db.DBMetaDataIO.TableInfo;
import com.cve.io.db.DBMetaDataIO.TableSpecifier;
import com.cve.io.db.driver.DefaultDBMetaData;
import com.cve.log.Log;
import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import static com.cve.log.Log.notNullArgs;
/**
 * Meta data driver for H2 database.
 * @author curt
 */
final class H2MetaData extends DefaultDBMetaData {


    private H2MetaData(DBMetaDataIO io, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        super(io,managedFunction,serversStore);
    }

    static DBMetaData of(DBConnection connection, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        DBMetaDataIO io = H2MetaDataIO.of(connection,managedFunction);
        return new H2MetaData(io,managedFunction,serversStore);
    }


    /**
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server) {
        notNullArgs(server);
        List<DBColumn> list = Lists.newArrayList();
        String           catalog = null;
        String     schemaPattern = null;
        String  tableNamePattern = null;
        String columnNamePattern = null;
        ColumnSpecifier specifier = ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        for (ColumnInfo info : dbmd.getColumns(specifier).value ) {
            debug(specifier + " -> " + info);
            String    tableName = info.tableName;
            String   columnName = info.columnName;
            String databaseName = info.tableSchema;
            Class          type = classFor(info.dataType);
            Database   database = Database.serverName(server, databaseName);
            DBColumn     column = database.tableName(tableName).columnNameType(columnName,type);
            list.add(column);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return CurrentValue.of(columns);
    }

    /**
     * See http://java.sun.com/javase/6/docs/api/java/sql/DatabaseMetaData.html#getColumns(java.lang.String,%20java.lang.String,%20java.lang.String,%20java.lang.String)
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) {
        DBServer server = database.server;
        List<DBColumn> list = Lists.newArrayList();
        String          catalog = null;
        String    schemaPattern = database.name;
        String tableNamePattern = null;
        String columnNamePattern = null;
        ColumnSpecifier specifier = ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        for (ColumnInfo info : dbmd.getColumns(specifier).value ) {
            String    tableName = info.tableName;
            String   columnName = info.columnName;
            Class          type = classFor(info.dataType);
            DBColumn     column = database.tableName(tableName).columnNameType(columnName,type);
            list.add(column);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return CurrentValue.of(columns);
    }

    /**
     * Nicer wrapper for
     * See http://java.sun.com/javase/6/docs/api/java/sql/DatabaseMetaData.html#getColumns(java.lang.String,%20java.lang.String,%20java.lang.String,%20java.lang.String)
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) {
        Database       database = table.database;
        DBServer           server = database.server;
        String          catalog = null;
        String    schemaPattern = database.name;
        String tableNamePattern = table.name;
        String columnNamePattern = null;
        List<DBColumn> list = Lists.newArrayList();
        ColumnSpecifier specifier = ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        for (ColumnInfo info : dbmd.getColumns(specifier).value ) {
            String   columnName = info.columnName;
            Class          type = classFor(info.dataType);
            DBColumn     column = table.columnNameType(columnName,type);
            list.add(column);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return CurrentValue.of(columns);
    }

    /**
     */
    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) {
        String          catalog = null;
        String    schemaPattern = database.name;
        String tableNamePattern = null;
        String[]          types = null;
        List<DBTable> list = Lists.newArrayList();
        TableSpecifier specifier = TableSpecifier.of(catalog, schemaPattern, tableNamePattern, types);
        for (TableInfo info : dbmd.getTables(specifier).value ) {
            String tableName = info.tableName;
            DBTable table = database.tableName(tableName);
            list.add(table);
        }
        ImmutableList<DBTable> tables = ImmutableList.copyOf(list);
        return CurrentValue.of(tables);
    }

    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server) {
        List<Database> list = Lists.newArrayList();
        for (SchemaInfo info : dbmd.getSchemas().value) {
            String databaseName = info.schemaName;
            list.add(server.databaseName(databaseName));
        }
        ImmutableList<Database> databases = ImmutableList.copyOf(list);
        return CurrentValue.of(databases);
    }

    /**
     * Logging stuff.
     */
    static final Log LOG = Log.of(DefaultDBMetaData.class);
    private static void info(String mesage) { LOG.info(mesage);  }
    private static void debug(String mesage) { LOG.debug(mesage);  }

}
