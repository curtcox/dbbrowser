package com.cve.db.dbio;

import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Server;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Curt
 */
final class MySQLResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public MySQLResultSetMetaDataFactory(Server server, ResultSetMetaData meta) {
        super(server,meta);
    }

    @Override
    public ImmutableList<Database> getDatabases() throws SQLException {
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
    public ImmutableList<DBTable> getTables() throws SQLException {
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
    public ImmutableList<DBColumn> getColumns() throws SQLException {
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
    public ImmutableList<AggregateFunction> getFunctions() throws SQLException {
        int count = meta.getColumnCount();
        List<AggregateFunction> list = Lists.newArrayList();
        for (int i=1; i<=count; i++) {
            list.add(AggregateFunction.IDENTITY);
        }
        return ImmutableList.copyOf(list);
    }
}
