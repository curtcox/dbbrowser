package com.cve.util;

import com.cve.web.db.DBURIParser;
import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Filter;
import com.cve.db.Join;
import com.cve.db.Order;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.Group;
import com.cve.db.Select;
import com.cve.db.Value;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class URIParserTest {

    @Test
    public void getAllFull() {
        String uri = "/server/db/db.t1+db.t2/db.t1.c1+db.t2.c2+count(c1)/db.t1.c1=db.t2.c2/db.t1.c1=only/db.t1.c1=ASC/db.t1.c1/";
        Server     server = Server.uri(URIs.of("server"));
        Database database = server.databaseName("db");
        DBTable          t1 = database.tableName("t1");
        DBTable          t2 = database.tableName("t2");
        DBColumn         c1 = t1.columnName("c1");
        DBColumn         c2 = t2.columnName("c2");
        Join         join = Join.of(c1, c2);
        Filter     filter = Filter.of(c1, Value.of("only"));
        Order       order = Order.ascending(c1);
        Group       group = Group.of(c1);
        ImmutableList<DBTable> tables = list(t1,t2);
        assertEquals(server,          DBURIParser.getServer(uri));
        assertEquals(database,        DBURIParser.getDatabase(uri));
        assertEquals(list(database),  DBURIParser.getDatabases(uri));
        assertEquals(tables,          DBURIParser.getTables(uri));
        assertEquals(list(c1,c2,c1),  DBURIParser.getColumns(tables,uri));
        assertEquals(list(join),      DBURIParser.getJoins(tables,uri));
        assertEquals(list(filter),    DBURIParser.getFilters(tables,uri));
        assertEquals(list(order),     DBURIParser.getOrders(tables,uri));
        assertEquals(list(group),     DBURIParser.getGroups(tables,uri));
        assertEquals(list(AggregateFunction.IDENTITY,AggregateFunction.IDENTITY,AggregateFunction.COUNT),
                     DBURIParser.getFunctions(tables,uri));
    }

    @Test
    public void getAllShortened() {
        String uri = "/server/db/db.t1+db.t2/c1+0c2+count(c1)/c1=0c2/c1=only/c1=ASC/c1/";
        Server     server = Server.uri(URIs.of("server"));
        Database database = server.databaseName("db");
        DBTable          t1 = database.tableName("t1");
        DBTable          t2 = database.tableName("t2");
        DBColumn         c1 = t1.columnName("c1");
        DBColumn         c2 = t2.columnName("c2");
        Join         join = Join.of(c1, c2);
        Filter     filter = Filter.of(c1, Value.of("only"));
        Order       order = Order.ascending(c1);
        Group       group = Group.of(c1);
        ImmutableList<DBTable> tables = list(t1,t2);
        assertEquals(server,         DBURIParser.getServer(uri));
        assertEquals(database,       DBURIParser.getDatabase(uri));
        assertEquals(list(database), DBURIParser.getDatabases(uri));
        assertEquals(tables,         DBURIParser.getTables(uri));
        assertEquals(list(c1,c2,c1), DBURIParser.getColumns(tables,uri));
        assertEquals(list(join),     DBURIParser.getJoins(tables,uri));
        assertEquals(list(filter),   DBURIParser.getFilters(tables,uri));
        assertEquals(list(order),    DBURIParser.getOrders(tables,uri));
        assertEquals(list(group),    DBURIParser.getGroups(tables,uri));
        assertEquals(list(AggregateFunction.IDENTITY,AggregateFunction.IDENTITY,AggregateFunction.COUNT),
                     DBURIParser.getFunctions(tables,uri));
    }

    @Test
    public void getServerWhenOnlyServer() {
        String uri = "/server/";
        Server     server = Server.uri(URIs.of("server"));
        assertEquals(server,  DBURIParser.getServer(uri));
    }

    @Test
    public void getDBsWhenOnlyServerWithSlash() {
        String uri = "/server/";
        assertEquals(list(),  DBURIParser.getDatabases(uri));
    }

    @Test
    public void getDBsWhenOnlyServerWithNoSlash() {
        String uri = "/server";
        assertEquals(list(),  DBURIParser.getDatabases(uri));
    }

    @Test
    public void getWithEmptyGroupBy() {
        String uri = "/SAMPLE/PUBLIC/PUBLIC.CITY/CITY_ID+CITY+COUNTRY_ID+LAST_UPDATE/////20+20/";
        Select select = DBURIParser.getSelect(uri);
        Server     server = Server.uri(URIs.of("SAMPLE"));
        Database database = server.databaseName("PUBLIC");
        assertEquals(database,select.databases.get(0));
        assertEquals(server,select.databases.get(0).server);
    }

    private static <T> ImmutableList<T> list(T... items) { return ImmutableList.of(items); }
}