package com.cve.web.db;

import com.cve.log.Log;
import com.cve.util.*;
import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Join;
import com.cve.model.db.Order;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.Group;
import com.cve.model.db.Select;
import com.cve.model.db.DBValue;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class DBURIParserTest {

    Log log;
    DBURICodec codec = DBURICodec.of(log);

    @Test
    public void getAllFull() {
        String uri = "//server/db/db.t1+db.t2/db.t1.c1+db.t2.c2+count(c1)/db.t1.c1=db.t2.c2/db.t1.c1=only/db.t1.c1=ASC/db.t1.c1/";
        DBServer     server = DBServer.uri(URIs.of("server"));
        Database database = server.databaseName("db");
        DBTable          t1 = database.tableName("t1");
        DBTable          t2 = database.tableName("t2");
        DBColumn         c1 = t1.columnName("c1");
        DBColumn         c2 = t2.columnName("c2");
        Join         join = Join.of(c1, c2);
        DBRowFilter     filter = DBRowFilter.of(c1, DBValue.of("only"),log);
        Order       order = Order.ascending(c1);
        Group       group = Group.of(c1);
        ImmutableList<DBTable> tables = list(t1,t2);
        assertEquals(server,          codec.getServer(uri));
        assertEquals(database,        codec.getDatabase(uri));
        assertEquals(list(database),  codec.getDatabases(uri));
        assertEquals(tables,          codec.getTables(uri));
        assertEquals(list(c1,c2,c1),  codec.getColumns(tables,uri));
        assertEquals(list(join),      codec.getJoins(tables,uri));
        assertEquals(list(filter),    codec.getFilters(tables,uri));
        assertEquals(list(order),     codec.getOrders(tables,uri));
        assertEquals(list(group),     codec.getGroups(tables,uri));
        assertEquals(list(AggregateFunction.IDENTITY,AggregateFunction.IDENTITY,AggregateFunction.COUNT),
                     codec.getFunctions(tables,uri));
    }

    @Test
    public void getAllShortened() {
        String uri = "//server/db/db.t1+db.t2/c1+0c2+count(c1)/c1=0c2/c1=only/c1=ASC/c1/";
        DBServer     server = DBServer.uri(URIs.of("server"));
        Database database = server.databaseName("db");
        DBTable          t1 = database.tableName("t1");
        DBTable          t2 = database.tableName("t2");
        DBColumn         c1 = t1.columnName("c1");
        DBColumn         c2 = t2.columnName("c2");
        Join         join = Join.of(c1, c2);
        DBRowFilter     filter = DBRowFilter.of(c1, DBValue.of("only"),log);
        Order       order = Order.ascending(c1);
        Group       group = Group.of(c1);
        ImmutableList<DBTable> tables = list(t1,t2);
        assertEquals(server,         codec.getServer(uri));
        assertEquals(database,       codec.getDatabase(uri));
        assertEquals(list(database), codec.getDatabases(uri));
        assertEquals(tables,         codec.getTables(uri));
        assertEquals(list(c1,c2,c1), codec.getColumns(tables,uri));
        assertEquals(list(join),     codec.getJoins(tables,uri));
        assertEquals(list(filter),   codec.getFilters(tables,uri));
        assertEquals(list(order),    codec.getOrders(tables,uri));
        assertEquals(list(group),    codec.getGroups(tables,uri));
        assertEquals(list(AggregateFunction.IDENTITY,AggregateFunction.IDENTITY,AggregateFunction.COUNT),
                     codec.getFunctions(tables,uri));
    }

    @Test
    public void getServerWhenOnlyServer() {
        String uri = "//server/";
        DBServer     server = DBServer.uri(URIs.of("server"));
        assertEquals(server,  codec.getServer(uri));
    }

    @Test
    public void getDBsWhenOnlyServerWithSlash() {
        String uri = "//server/";
        assertEquals(list(),  codec.getDatabases(uri));
    }

    @Test
    public void getDBsWhenOnlyServerWithNoSlash() {
        String uri = "//server";
        assertEquals(list(),  codec.getDatabases(uri));
    }

    @Test
    public void getWithEmptyGroupBy() {
        String uri = "//SAMPLE/PUBLIC/PUBLIC.CITY/CITY_ID+CITY+COUNTRY_ID+LAST_UPDATE/////20+20/";
        Select select = codec.getSelect(uri);
        DBServer     server = DBServer.uri(URIs.of("SAMPLE"));
        Database database = server.databaseName("PUBLIC");
        assertEquals(database,select.databases.get(0));
        assertEquals(server,select.databases.get(0).server);
    }

    private static <T> ImmutableList<T> list(T... items) { return ImmutableList.of(items); }
}