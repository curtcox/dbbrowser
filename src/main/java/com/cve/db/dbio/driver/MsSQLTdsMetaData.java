package com.cve.db.dbio.driver;

import com.cve.db.dbio.*;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.dbio.DBMetaDataIO.ColumnInfo;
import com.cve.db.dbio.DBMetaDataIO.ColumnSpecifier;
import com.cve.db.dbio.DBMetaDataIO.TableInfo;
import com.cve.db.dbio.DBMetaDataIO.TableSpecifier;
import com.cve.stores.CurrentValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Server server) throws SQLException {
        DBMetaDataIO   dbmd = getDbmdIO(server);
        List<DBColumn> list = Lists.newArrayList();
        for (Database database : getDatabasesOn(server).value) {
            String          catalog = null;
            String    schemaPattern = null;
            String tableNamePattern = null;
            String columnNamePattern = null;
            for (ColumnInfo info : dbmd.getColumns(ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern)).value) {
                String  tableName = info.tableName;
                String columnName = info.columnName;
                DBColumn column = database.tableName(tableName).columnName(columnName);
                if (!isSystemColumn(column)) {
                    list.add(column);
                }
            }
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return CurrentValue.of(columns);
    }

    /**
     * Simple cache
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) throws SQLException {
        Database       database = table.database;
        Server           server = database.server;
        DBMetaDataIO       dbmd = getDbmdIO(server);
        String          catalog = database.name;
        String    schemaPattern = null;
        String tableNamePattern = table.name;
        String columnNamePattern = null;
        List<DBColumn> list = Lists.newArrayList();
        for (ColumnInfo info : dbmd.getColumns(ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern)).value) {
            String  tableName = info.tableName;
            String columnName = info.columnName;
            DBColumn column = database.tableName(tableName).columnName(columnName);
            if (!isSystemColumn(column)) {
                list.add(column);
            }
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
        String          catalog = database.name;
        String    schemaPattern = null;
        String tableNamePattern = null;
        String[]          types = null;
        List<DBTable> list = Lists.newArrayList();
        for (TableInfo info : dbmd.getTables(TableSpecifier.of(catalog, schemaPattern, tableNamePattern, types)).value) {
            String tableName = info.tableName;
            DBTable table = database.tableName(tableName);
            if (!isSystemTable(table)) {
                list.add(table);
            }
        }
        ImmutableList<DBTable> tables = ImmutableList.copyOf(list);
        return CurrentValue.of(tables);
    }

    static boolean isSystemTable(DBTable table) {
        return table.name.startsWith("sys");
    }

    static boolean isSystemColumn(DBColumn column) {
        return column.table.name.startsWith("sys");
    }
}
