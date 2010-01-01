package com.cve.db.dbio.driver;

import com.cve.db.dbio.*;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.dbio.DBMetaDataIO.ColumnInfo;
import com.cve.db.dbio.DBMetaDataIO.ColumnSpecifier;
import com.cve.db.dbio.DBMetaDataIO.SchemaInfo;
import com.cve.db.dbio.DBMetaDataIO.TableInfo;
import com.cve.db.dbio.DBMetaDataIO.TableSpecifier;
import com.cve.stores.CurrentValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.SQLException;
import java.util.List;

/**
 * Meta data driver for H2 database.
 * @author curt
 */
final class H2MetaData extends DefaultDBMetaData {

    private H2MetaData() {}

    static DBMetaData of() {
        return new H2MetaData();
    }

    /**
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Server server) throws SQLException {
        DBMetaDataIO   dbmd = getDbmdIO(server);
        List<DBColumn> list = Lists.newArrayList();
        String           catalog = null;
        String     schemaPattern = null;
        String  tableNamePattern = null;
        String columnNamePattern = null;
        ColumnSpecifier specifier = ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        for (ColumnInfo info : dbmd.getColumns(specifier).value ) {
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
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) throws SQLException {
        Server server = database.server;
        DBMetaDataIO   dbmd = getDbmdIO(server);
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
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) throws SQLException {
        Database       database = table.database;
        Server           server = database.server;
        DBMetaDataIO       dbmd = getDbmdIO(server);
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
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database)  throws SQLException {
        Server           server = database.server;
        DBMetaDataIO       dbmd = getDbmdIO(server);
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
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(Server server)  throws SQLException {
        DBMetaDataIO  dbmd = getDbmdIO(server);
        List<Database> list = Lists.newArrayList();
        for (SchemaInfo info : dbmd.getSchemas().value) {
            String databaseName = info.schemaName;
            list.add(server.databaseName(databaseName));
        }
        ImmutableList<Database> databases = ImmutableList.copyOf(list);
        return CurrentValue.of(databases);
    }
}
