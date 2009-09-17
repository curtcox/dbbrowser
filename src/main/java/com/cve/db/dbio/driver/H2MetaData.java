package com.cve.db.dbio.driver;

import com.cve.db.dbio.*;
import com.cve.db.DBColumn;
import com.cve.db.DBColumn.Keyness;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Server;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.ResultSet;
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
        String          catalog = null;
        String    schemaPattern = null;
        String tableNamePattern = null;
        String columnNamePattern = null;
        ResultSet results = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            while (results.next()) {
                String tableName = results.getString("TABLE_NAME");
                String columnName = results.getString("COLUMN_NAME");
                // Due to a driver bug, we can't use the column name
                int TABLE_SCHEMA = 2;
                String databaseName = results.getString(TABLE_SCHEMA);
                Class        type = classFor(results.getInt("DATA_TYPE"));
                Database database = Database.serverName(server, databaseName);
                DBColumn column = database.tableName(tableName).columnNameType(columnName,type);
                list.add(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
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
        ResultSet results = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            while (results.next()) {
                String  tableName = results.getString("TABLE_NAME");
                String columnName = results.getString("COLUMN_NAME");
                Class        type = classFor(results.getInt("DATA_TYPE"));
                DBColumn column = database.tableName(tableName).columnNameType(columnName,type);
                list.add(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
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
        ResultSet results = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            List<DBColumn> list = Lists.newArrayList();
            while (results.next()) {
                String columnName = results.getString("COLUMN_NAME");
                Class        type = classFor(results.getInt("DATA_TYPE"));
                Keyness   keyness = keyness(table,columnName);
                list.add(table.keynessColumnNameType(keyness,columnName,type));
            }
            ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
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
        ResultSet results = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);
        try {
            List<DBTable> list = Lists.newArrayList();
            while (results.next()) {
                String tableName   = results.getString("TABLE_NAME");
                DBTable table = database.tableName(tableName);
                list.add(table);
            }
            ImmutableList<DBTable> tables = ImmutableList.copyOf(list);
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<Database> getDatabasesOn(Server server)  throws SQLException {
        DBMetaDataIO  dbmd = getDbmdIO(server);
        ResultSet  results = dbmd.getSchemas();
        try {
            List<Database> list = Lists.newArrayList();
            while (results.next()) {
                // Due to a driver bug, we can't use the column name
                int SCHEMA_NAME = 1;
                String databaseName = results.getString(SCHEMA_NAME);
                list.add(server.databaseName(databaseName));
            }
            ImmutableList<Database> databases = ImmutableList.copyOf(list);
            return databases;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }
}
