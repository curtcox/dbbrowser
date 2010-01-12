package com.cve.io.db.driver.mysql;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Curt
 */
final class MySQLResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public MySQLResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

    @Override
    public ImmutableList<Database> getDatabases() {
        int count = meta.getColumnCount();
        Set<Database> set = Sets.newHashSet();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.getCatalogName(i);
            Database database   = server.databaseName(databaseName);
            set.add(database);
        }
        return ImmutableList.copyOf(set);
    }

    @Override
    public ImmutableList<DBTable> getTables() {
        int count = meta.getColumnCount();
        Set<DBTable> set = Sets.newHashSet();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.getCatalogName(i);
            String tableName    = meta.getTableName(i);
            DBTable   table     = server.databaseName(databaseName).tableName(tableName);
            set.add(table);
        }
        return ImmutableList.copyOf(set);
    }

    @Override
    public ImmutableList<DBColumn> getColumns() {
        int count = meta.getColumnCount();
        List<DBColumn> list = Lists.newArrayList();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.getCatalogName(i);
            String tableName    = meta.getTableName(i);
            String columnName   = meta.getColumnName(i);
            DBColumn column     = server.databaseName(databaseName).tableName(tableName).columnName(columnName);
            list.add(column);
        }
        return ImmutableList.copyOf(list);
    }

    @Override
    public ImmutableList<AggregateFunction> getFunctions() {
        int count = meta.getColumnCount();
        List<AggregateFunction> list = Lists.newArrayList();
        for (int i=1; i<=count; i++) {
            list.add(AggregateFunction.IDENTITY);
        }
        return ImmutableList.copyOf(list);
    }
}
