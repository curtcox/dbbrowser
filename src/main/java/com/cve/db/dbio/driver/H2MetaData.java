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
    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        DBMetaDataIO   dbmd = getDbmdIO(server);
        List<DBColumn> list = Lists.newArrayList();
        String           catalog = null;
        String     schemaPattern = null;
        String  tableNamePattern = null;
        String columnNamePattern = null;
        for (ColumnInfo info : dbmd.getColumns(ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern)) ) {
            String    tableName = info.tableName;
            String   columnName = info.columnName;
            String databaseName = info.tableSchema;
            Class          type = classFor(info.dataType);
            Database   database = Database.serverName(server, databaseName);
            DBColumn     column = database.tableName(tableName).columnNameType(columnName,type);
            list.add(column);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return columns;
    }

    /**
     * See http://java.sun.com/javase/6/docs/api/java/sql/DatabaseMetaData.html#getColumns(java.lang.String,%20java.lang.String,%20java.lang.String,%20java.lang.String)
     */
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Database database) throws SQLException {
        Server server = database.server;
        DBMetaDataIO   dbmd = getDbmdIO(server);
        List<DBColumn> list = Lists.newArrayList();
        String          catalog = null;
        String    schemaPattern = database.name;
        String tableNamePattern = null;
        String columnNamePattern = null;
        for (ColumnInfo info : dbmd.getColumns(ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern)) ) {
            String    tableName = info.tableName;
            String   columnName = info.columnName;
            Class          type = classFor(info.dataType);
            DBColumn     column = database.tableName(tableName).columnNameType(columnName,type);
            list.add(column);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return columns;
    }

    /**
     * Nicer wrapper for
     * See http://java.sun.com/javase/6/docs/api/java/sql/DatabaseMetaData.html#getColumns(java.lang.String,%20java.lang.String,%20java.lang.String,%20java.lang.String)
     */
    @Override
    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        Database       database = table.database;
        Server           server = database.server;
        DBMetaDataIO       dbmd = getDbmdIO(server);
        String          catalog = null;
        String    schemaPattern = database.name;
        String tableNamePattern = table.name;
        String columnNamePattern = null;
        List<DBColumn> list = Lists.newArrayList();
        for (ColumnInfo info : dbmd.getColumns(ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern)) ) {
            String   columnName = info.columnName;
            Class          type = classFor(info.dataType);
            DBColumn     column = table.columnNameType(columnName,type);
            list.add(column);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return columns;
    }

    /**
     */
    @Override
    public ImmutableList<DBTable> getTablesOn(Database database)  throws SQLException {
        Server           server = database.server;
        DBMetaDataIO       dbmd = getDbmdIO(server);
        String          catalog = null;
        String    schemaPattern = database.name;
        String tableNamePattern = null;
        String[]          types = null;
        List<DBTable> list = Lists.newArrayList();
        for (TableInfo info : dbmd.getTables(catalog, schemaPattern, tableNamePattern, types) ) {
            String tableName = info.tableName;
            DBTable table = database.tableName(tableName);
            list.add(table);
        }
        ImmutableList<DBTable> tables = ImmutableList.copyOf(list);
        return tables;
    }

    @Override
    public ImmutableList<Database> getDatabasesOn(Server server)  throws SQLException {
        DBMetaDataIO  dbmd = getDbmdIO(server);
        List<Database> list = Lists.newArrayList();
        for (SchemaInfo info : dbmd.getSchemas()) {
            String databaseName = info.schemaName;
            list.add(server.databaseName(databaseName));
        }
        ImmutableList<Database> databases = ImmutableList.copyOf(list);
        return databases;
    }
}
