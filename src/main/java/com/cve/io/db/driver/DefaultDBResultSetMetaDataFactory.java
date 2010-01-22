package com.cve.io.db.driver;

import com.cve.io.db.DBResultSetIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.DBResultSetMetaData;
import com.cve.io.db.DBConnection;
import com.cve.log.Log;
import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import static com.cve.util.Check.notNull;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import javax.annotation.concurrent.Immutable;


/**
 * For generating result set meta data.
 * @author curt
 */
@Immutable
public class DefaultDBResultSetMetaDataFactory {

    public final DBServer server;

    public final DBResultSetMetaDataIO meta;

    final Log log;

    protected DefaultDBResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta, Log log) {
        this.server = notNull(server);
        this.meta = notNull(meta);
        this.log = notNull(log);
    }

    public DBResultSetMetaData of(DBServer server, DBConnection connection, DBResultSetIO results) {
        log.notNullArgs(server,connection,results);
        DefaultDBResultSetMetaDataFactory factory = factory(server,connection,results);
        DBResultSetMetaData meta = DBResultSetMetaData.of(factory.getDatabases(),factory.getTables(),factory.getColumns(),factory.getFunctions());
        return meta;
    }

    private static DefaultDBResultSetMetaDataFactory factory(DBServer server, DBConnection connection, DBResultSetIO results, Log log) {
        log.notNullArgs(server,connection,results);
        DBDriver driver = connection.getInfo().driver;
        DBResultSetMetaDataIO meta = results.meta;
        return driver.getResultSetFactory(server, meta,log);
    }

    public ImmutableList<Database> getDatabases() {
        int count = meta.columnCount;
        Set<Database> set = Sets.newHashSet();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.schemaNames.get(i);
            Database database   = server.databaseName(databaseName);
            set.add(database);
        }
        return ImmutableList.copyOf(set);
    }

    public ImmutableList<DBTable> getTables() {
        int count = meta.columnCount;
        Set<DBTable> set = Sets.newHashSet();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.schemaNames.get(i);
            String tableName    = meta.tableNames.get(i);
            DBTable   table     = server.databaseName(databaseName).tableName(tableName);
            set.add(table);
        }
        return ImmutableList.copyOf(set);
    }

    public ImmutableList<DBColumn> getColumns() {
        int count = meta.columnCount;
        List<DBColumn> list = Lists.newArrayList();
        for (int i=1; i<=count; i++) {
            String databaseName = meta.schemaNames.get(i);
            String tableName    = meta.tableNames.get(i);
            String columnName   = meta.columnNames.get(i);
            DBColumn column     = server.databaseName(databaseName).tableName(tableName).columnName(columnName);
            list.add(column);
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<AggregateFunction> getFunctions() {
        int count = meta.columnCount;
        List<AggregateFunction> list = Lists.newArrayList();
        for (int i=1; i<=count; i++) {
            list.add(AggregateFunction.IDENTITY);
        }
        return ImmutableList.copyOf(list);
    }

}
