
package com.cve.web.db;

import com.cve.util.*;
import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Join;
import com.cve.model.db.DBLimit;
import com.cve.model.db.Order;
import com.cve.model.db.Select;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.Group;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;
/**
 * Tools for converting between objects and the specially formatted database
 * {@link URI}S we work with.
 * <p>
 * What is the special format?
 * The URL space hierarcy is mapped onto URLs like:
 * http://webserver/search/dbserver/databases/tables/columns/joins/filters/orders/groups/limit
 * Empty parts are preserved as an empty path element like filters, orders, and
 * groups in the following:
 * http://webserver/+/dbserver/databases/tables/columns/joins////limit
 *
 */
public final class DBURICodec {

    /**
     * The position within the URI where the given information is specified.
     * Most of these map very directly to clauses in SQL select statements.
     */
    enum Position {

        /**
         * The optional search text.
         */
        SEARCH(1),

        /**
         * The database server.
         */
        SERVER(2),
        
        /**
         * The databases.
         */
        DBS(3),

        /**
         * The metadata method name.
         * This isn't used in normal operations.
         * It is for low-level JDBC debugging and experiments.
         */
        METADATA(3),

        /**
         * The database tables.
         */
        TABLES(4),

        /**
         * The table columns.
         */
        COLUMNS(5),

        /**
         * Joins between tables.
         */
        JOINS(6),

        /**
         * Filters on column values
         */
        FILTERS(7),

        /**
         * Column sort orders
         */
        ORDERS(8),

        /**
         * Group by clauses
         */
        GROUPS(9),

        /**
         * Limits on what rows should be returned.
         */
        LIMIT(10),
    ;

        /**
         * Number of slashes before this position in the URL.
         * For example:
         *                    1        2        3        4
         * http://webserver/search/dbserver/databases/tables
         */
        private final int index;

        Position(int index) {
            this.index = index;
        }

    }

    final Log log = Logs.of();

    /**
     * Use the factory.
     */
    private DBURICodec() {
        
    }

    public static DBURICodec of() {
        return new DBURICodec();
    }

    String at(String uri, Position pos) {
        log.args(uri);
        return uri.split("/")[pos.index];
    }

    boolean exists(String uri, Position pos) {
        String[] parts = uri.split("/");
        return parts.length > pos.index;
    }

    public Search getSearch(String uri) {
        log.args(uri);
        if (uri.equals("/")) {
            return Search.EMPTY;
        }
        String search = URLDecoder.decode(at(uri,Position.SEARCH));
        return Search.parse(search);
    }

    public DBServer getServer(String uri) {
        log.args(uri);
        String name = at(uri,Position.SERVER);
        return DBServer.uri(URIs.of(name));
    }

    public Database getDatabase(String uri) {
        log.args(uri);
        DBServer server = DBServer.uri(URIs.of(at(uri,Position.SERVER)));
        Database database = server.databaseName(at(uri,Position.DBS));
        return database;
    }

    public String getMetaDataMethod(String uri) {
        log.args(uri);
        if (!exists(uri,Position.METADATA)) {
            return "";
        }
        return at(uri,Position.METADATA);
    }

    public ImmutableList<Database> getDatabases(String uri) {
        log.args(uri);
        if (!exists(uri,Position.DBS)) {
            return ImmutableList.of();
        }
        DBServer server = DBServer.uri(URIs.of(at(uri,Position.SERVER)));
        List<Database> list = Lists.newArrayList();
        for (String databaseName : at(uri,Position.DBS).split("\\+")) {
            list.add(server.databaseName(databaseName));
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<DBTable> getTables(String uri) {
        log.args(uri);
        if (!exists(uri,Position.TABLES)) {
            return ImmutableList.of();
        }
        DBServer server = DBServer.uri(URIs.of(at(uri,Position.SERVER)));
        List<DBTable> list = Lists.newArrayList();
        for (String fullTableName : at(uri,Position.TABLES).split("\\+")) {
            DBTable        table = DBTable.parse(server,fullTableName);
            list.add(table);
        }
        return ImmutableList.copyOf(list);
    }

    /**
     * Get columns from
     * /server/dbs/tables/columns
     */
    public ImmutableList<DBColumn> getColumns(ImmutableList<DBTable> tables, String uri) {
        if (!exists(uri,Position.COLUMNS)) {
            return ImmutableList.of();
        }
        DBServer server = DBServer.uri(URIs.of(at(uri,Position.SERVER)));
        List<DBColumn> list = Lists.newArrayList();
        for (String fullColumnName : at(uri,Position.COLUMNS).split("\\+")) {
            fullColumnName = splitFullColumnName(fullColumnName)[1];
            DBColumn column = DBColumn.parse(server,tables,fullColumnName);
            list.add(column);
        }
        return ImmutableList.copyOf(list);
    }

    /**
     * Tokenize the column name and optional aggregate function into two
     * parts.
     */
    private static String[] splitFullColumnName(String full) {
        String[] parts = full.split("\\(|\\)");
        if (parts.length==1) {
            return new String[] { "", parts[0] };
        }
        if (parts.length==2) {
            return parts;
        }
        throw new IllegalArgumentException(full);
    }

    public ImmutableList<AggregateFunction> getFunctions(ImmutableList<DBTable> tables, String uri) {
        log.args(tables,uri);
        if (!exists(uri,Position.COLUMNS)) {
            return ImmutableList.of();
        }
        List<AggregateFunction> list = Lists.newArrayList();
        for (String fullColumnName : at(uri,Position.COLUMNS).split("\\+")) {
            fullColumnName = splitFullColumnName(fullColumnName)[0];
            AggregateFunction function = AggregateFunction.parse(fullColumnName);
            list.add(function);
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<Join> getJoins(ImmutableList<DBTable> tables,String uri) {
        log.args(tables,uri);
        if (!exists(uri,Position.JOINS)) {
            return ImmutableList.of();
        }
        String joinParts = at(uri,Position.JOINS);
        if (joinParts.length()==0) {
            return ImmutableList.of();
        }
        DBServer server = DBServer.uri(URIs.of(at(uri,Position.SERVER)));
        List<Join> list = Lists.newArrayList();
        for (String fullJoinName : joinParts.split("\\+")) {
            Join join = Join.parse(server,tables,fullJoinName);
            list.add(join);
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<DBRowFilter> getFilters(ImmutableList<DBTable> tables,String uri) {
        log.args(tables,uri);
        if (!exists(uri,Position.FILTERS)) {
            return ImmutableList.of();
        }
        String filterParts = at(uri,Position.FILTERS);
        if (filterParts.length()==0) {
            return ImmutableList.of();
        }

        DBServer server = DBServer.uri(URIs.of(at(uri,Position.SERVER)));
        List<DBRowFilter> list = Lists.newArrayList();
        for (String fullFilterName : filterParts.split("\\+")) {
            DBRowFilter filter = DBRowFilter.parse(server,tables,fullFilterName);
            list.add(filter);
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<Order> getOrders(ImmutableList<DBTable> tables,String uri) {
        log.args(tables,uri);
        if (!exists(uri,Position.ORDERS)) {
            return ImmutableList.of();
        }
        String orderParts = at(uri,Position.ORDERS);
        if (orderParts.length()==0) {
            return ImmutableList.of();
        }

        DBServer server = DBServer.uri(URIs.of(at(uri,Position.SERVER)));
        List<Order> list = Lists.newArrayList();
        for (String fullOrderName : orderParts.split("\\+")) {
            Order order = Order.parse(server,tables,fullOrderName);
            list.add(order);
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<Group> getGroups(ImmutableList<DBTable> tables,String uri) {
        log.args(tables,uri);
        if (!exists(uri,Position.GROUPS)) {
            return ImmutableList.of();
        }
        String orderParts = at(uri,Position.GROUPS);
        if (orderParts.length()==0) {
            return ImmutableList.of();
        }

        DBServer server = DBServer.uri(URIs.of(at(uri,Position.SERVER)));
        List<Group> list = Lists.newArrayList();
        for (String fullOrderName : orderParts.split("\\+")) {
            Group group = Group.parse(server,tables,fullOrderName);
            list.add(group);
        }
        return ImmutableList.copyOf(list);
    }

    DBLimit getLimit(String uri) {
        if (!exists(uri,Position.LIMIT)) {
            return DBLimit.DEFAULT;
        }
        String[] limitParts = at(uri,Position.LIMIT).split("\\+");
        int  limit = Integer.parseInt(limitParts[0]);
        int offest = Integer.parseInt(limitParts[1]);
        return DBLimit.limitOffset(limit, offest);
    }

    public Select getSelect(String uri) {
        log.args(uri);
        // get everything out of the URL
        ImmutableList<Database>          databases = getDatabases(uri);
        ImmutableList<DBTable>              tables = getTables(uri);
        ImmutableList<DBColumn>            columns = getColumns(tables,uri);
        ImmutableList<AggregateFunction> functions = getFunctions(tables,uri);
        ImmutableList<Join>                  joins = getJoins(tables,uri);
        ImmutableList<DBRowFilter>              filters = getFilters(tables,uri);
        ImmutableList<Order>                orders = getOrders(tables,uri);
        ImmutableList<Group>                groups = getGroups(tables,uri);
        DBLimit                                limit = getLimit(uri);
        validateRequest(databases,tables,columns,joins,filters,orders);

        // Setup the select
        Select select = Select.from(databases, tables, columns, functions, joins,filters,orders,groups,limit);
        return select;
    }

    /**
     * There is a lot more request checking we should do, but for now
     * we just...
     */
    static void validateRequest(ImmutableList<Database> databases,
        ImmutableList<DBTable>       tables,  ImmutableList<DBColumn>     columns,
        ImmutableList<Join>         joins,  ImmutableList<DBRowFilter>     filters,
        ImmutableList<Order>       orders)
    {
        validateMinimumSize(databases,1);
        validateMinimumSize(tables,1);
    }

    static void validateMinimumSize(List<?> list, int minimum) {
        if (list.size() < minimum) {
            String message = "Must contain at least " + minimum + " elements, but doesn't " + list;
            throw new IllegalArgumentException(message);
        }
    }


    public static URI encode(Search search) {
        return URIs.of("/" + search.toUrlFragment() + "/");
    }

    /**
     * Return a URI for browsing the given server.
     */
    public static URI encode(DBServer server) {
        Search search = Search.EMPTY;
        return URIs.of(encode(search) + server.uri.toString() + "/");
    }

    /**
     * Return a URI for browsing the given server.
     */
    public static URI encode(Search search, DBServer server) {
        return URIs.of(encode(search) + server.uri.toString() + "/");
    }

    public URI encode(Database database) {
        log.args(database);
        return encode(Search.EMPTY,database);
    }

    public URI encode(Search search, Database database) {
        log.args(search,database);
        DBServer server = database.server;
        return URIs.of(encode(search,server) + database.name + "/");
    }

    public URI encode(Search search, DBTable table) {
        return URIs.of(encode(search,table.database) + table.fullName() + "/");
    }

    public URI encode(DBTable table) {
        return encode(Search.EMPTY,table);
    }

    public URI encode(Search search, DBColumn column) {
        return URIs.of(encode(search,column.table) + column.fullName() + "/");
    }

    public URI encode(DBColumn column) {
        return encode(Search.EMPTY,column);
    }

}
