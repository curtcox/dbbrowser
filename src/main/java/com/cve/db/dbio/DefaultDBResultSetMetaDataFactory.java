package com.cve.db.dbio;

import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * For generating result set meta data.
 * @author curt
 */
class DefaultDBResultSetMetaDataFactory {

    private final Server server;

    private final ResultSetMetaData meta;

    private DefaultDBResultSetMetaDataFactory(Server server, ResultSetMetaData meta) {
        this.server = Check.notNull(server);
        this.meta = Check.notNull(meta);
    }

    static DBResultSetMetaData of(Server server, DBConnection connection, ResultSet results) throws SQLException {
        DefaultDBResultSetMetaDataFactory factory = factory(server,connection,results);
        return DBResultSetMetaData.of(factory.getDatabases(),factory.getTables(),factory.getColumns(),factory.getFunctions());
    }

    private static DefaultDBResultSetMetaDataFactory factory(Server server, DBConnection connection, ResultSet results) throws SQLException {
        DBDriver driver = connection.info.driver;
        ResultSetMetaData meta = results.getMetaData();
        if (driver==DBDriver.MySql) {
            return new DefaultDBResultSetMetaDataFactory(server,meta);
        }
        throw new UnsupportedOperationException("" + driver);
    }

    public ImmutableList<Database> getDatabases() throws SQLException {
        int count = meta.getColumnCount();
        Set<Database> set = Sets.newHashSet();
        for (int i=0; i<count; i++) {
            String databaseName = meta.getSchemaName(i);
            Database database   = server.databaseName(databaseName);
            set.add(database);
        }
        return ImmutableList.copyOf(set);
    }

    public ImmutableList<DBTable> getTables() throws SQLException {
        int count = meta.getColumnCount();
        Set<DBTable> set = Sets.newHashSet();
        for (int i=0; i<count; i++) {
            String databaseName = meta.getSchemaName(i);
            String tableName    = meta.getTableName(i);
            DBTable   table     = server.databaseName(databaseName).tableName(tableName);
            set.add(table);
        }
        return ImmutableList.copyOf(set);
    }

    public ImmutableList<DBColumn> getColumns() throws SQLException {
        int count = meta.getColumnCount();
        List<DBColumn> list = Lists.newArrayList();
        for (int i=0; i<count; i++) {
            String databaseName = meta.getSchemaName(i);
            String tableName    = meta.getTableName(i);
            String columnName   = meta.getColumnName(i);
            DBColumn column     = server.databaseName(databaseName).tableName(tableName).columnName(columnName);
            list.add(column);
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<AggregateFunction> getFunctions() throws SQLException {
        int count = meta.getColumnCount();
        List<AggregateFunction> list = Lists.newArrayList();
        for (int i=0; i<count; i++) {
            list.add(AggregateFunction.IDENTITY);
        }
        return ImmutableList.copyOf(list);
    }

}
