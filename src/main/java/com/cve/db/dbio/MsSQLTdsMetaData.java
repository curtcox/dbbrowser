package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Server;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author curt
 */
final class MsSQLTdsMetaData extends DefaultDBMetaData {

    private MsSQLTdsMetaData() {}

    static DBMetaData of() {
        return new MsSQLTdsMetaData();
    }

    /**
     */
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Server server) throws SQLException {
        DBMetaDataIO   dbmd = getDbmdIO(server);
        List<DBColumn> list = Lists.newArrayList();
        for (Database database : getDatabasesOn(server)) {
            String          catalog = null;
            String    schemaPattern = null;
            String tableNamePattern = null;
            String columnNamePattern = null;
            ResultSet results = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            try {
                while (results.next()) {
                    String tableName = results.getString("TABLE_NAME");
                    String columnName = results.getString("COLUMN_NAME");
                    DBColumn column = database.tableName(tableName).columnName(columnName);
                    if (!isSystemColumn(column)) {
                        list.add(column);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                close(results);
            }
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return columns;
    }

    /**
     * Simple cache
     */
    @Override
    public ImmutableList<DBColumn> getColumnsFor(DBTable table) throws SQLException {
        Database       database = table.getDatabase();
        Server           server = database.getServer();
        DBMetaDataIO       dbmd = getDbmdIO(server);
        String          catalog = database.getName();
        String    schemaPattern = null;
        String tableNamePattern = table.getName();
        String columnNamePattern = null;
        ResultSet results = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            List<DBColumn> list = Lists.newArrayList();
            while (results.next()) {
                String columnName = results.getString("COLUMN_NAME");
                DBColumn column = table.columnName(columnName);
                if (!isSystemColumn(column)) {
                    list.add(column);
                }
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
        Server           server = database.getServer();
        DBMetaDataIO       dbmd = getDbmdIO(server);
        String          catalog = database.getName();
        String    schemaPattern = null;
        String tableNamePattern = null;
        String[]          types = null;
        ResultSet results = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);
        try {
            List<DBTable> list = Lists.newArrayList();
            while (results.next()) {
                String tableName = results.getString("TABLE_NAME");
                DBTable table = database.tableName(tableName);
                if (!isSystemTable(table)) {
                    list.add(table);
                }
            }
            ImmutableList<DBTable> tables = ImmutableList.copyOf(list);
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    static boolean isSystemTable(DBTable table) {
        return table.getName().startsWith("sys");
    }

    static boolean isSystemColumn(DBColumn column) {
        return column.getTable().getName().startsWith("sys");
    }
}
