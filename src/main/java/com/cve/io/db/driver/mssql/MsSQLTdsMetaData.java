package com.cve.io.db.driver.mssql;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBMetaDataIO;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBMetaDataIO.ColumnInfo;
import com.cve.io.db.DBMetaDataIO.ColumnSpecifier;
import com.cve.io.db.DBMetaDataIO.TableInfo;
import com.cve.io.db.DBMetaDataIO.TableSpecifier;
import com.cve.io.db.driver.DefaultDBMetaData;
import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 * @author curt
 */
final class MsSQLTdsMetaData extends DefaultDBMetaData {

    final DBMetaDataIO dbmd;

    private MsSQLTdsMetaData(DBMetaDataIO dbmd,ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        super(managedFunction,serversStore);
        this.dbmd = dbmd;
    }

    static DBMetaData of(DBMetaDataIO dbmd,ManagedFunction.Factory managedFunction,DBServersStore serversStore) {
        return new MsSQLTdsMetaData(dbmd,managedFunction,serversStore);
    }

    /**
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server) {
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
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) {
        Database       database = table.database;
        DBServer           server = database.server;
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
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) {
        DBServer           server = database.server;
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
