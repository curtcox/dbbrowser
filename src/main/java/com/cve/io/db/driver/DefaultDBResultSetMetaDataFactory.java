package com.cve.io.db.driver;

import com.cve.io.db.DBResultSetIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.DBResultSetMetaData;
import com.cve.io.db.DBConnection;
import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import javax.annotation.concurrent.Immutable;

import static com.cve.log.Log.args;

/**
 * For generating result set meta data.
 * @author curt
 */
@Immutable
public class DefaultDBResultSetMetaDataFactory {

    public final DBServer server;

    public final DBResultSetMetaDataIO meta;

    protected DefaultDBResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta) {
        this.server = Check.notNull(server);
        this.meta = Check.notNull(meta);
    }

    public static DBResultSetMetaData of(DBServer server, DBConnection connection, DBResultSetIO results) {
        args(server,connection,results);
        DefaultDBResultSetMetaDataFactory factory = factory(server,connection,results);
        DBResultSetMetaData meta = DBResultSetMetaData.of(factory.getDatabases(),factory.getTables(),factory.getColumns(),factory.getFunctions());
        return meta;
    }

    private static DefaultDBResultSetMetaDataFactory factory(DBServer server, DBConnection connection, DBResultSetIO results) {
        args(server,connection,results);
        DBDriver driver = connection.getInfo().driver;
        DBResultSetMetaDataIO meta = results.meta;
        return driver.getResultSetFactory(server, meta);
    }

    public ImmutableList<Database> getDatabases() {
        int count = meta.getColumnCount();
        Set<Database> set = Sets.newHashSet();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.getSchemaName(i);
            Database database   = server.databaseName(databaseName);
            set.add(database);
        }
        return ImmutableList.copyOf(set);
    }

    public ImmutableList<DBTable> getTables() {
        int count = meta.getColumnCount();
        Set<DBTable> set = Sets.newHashSet();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.getSchemaName(i);
            String tableName    = meta.getTableName(i);
            DBTable   table     = server.databaseName(databaseName).tableName(tableName);
            set.add(table);
        }
        return ImmutableList.copyOf(set);
    }

    public ImmutableList<DBColumn> getColumns() {
        int count = meta.getColumnCount();
        List<DBColumn> list = Lists.newArrayList();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.getSchemaName(i);
            String tableName    = meta.getTableName(i);
            String columnName   = meta.getColumnName(i);
            DBColumn column     = server.databaseName(databaseName).tableName(tableName).columnName(columnName);
            list.add(column);
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<AggregateFunction> getFunctions() {
        int count = meta.getColumnCount();
        List<AggregateFunction> list = Lists.newArrayList();
        for (int i=1; i<=count; i++) {
            list.add(AggregateFunction.IDENTITY);
        }
        return ImmutableList.copyOf(list);
    }

}
