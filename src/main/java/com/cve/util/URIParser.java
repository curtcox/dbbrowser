
package com.cve.util;

import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Filter;
import com.cve.db.Join;
import com.cve.db.Limit;
import com.cve.db.Order;
import com.cve.db.Select;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.Group;
import com.cve.log.Log;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;
import static com.cve.util.Check.notNull;

/**
 * Tools for working with {@link URI}S.
 */
public final class URIParser {

    enum Position {
        /**
         * The database server.
         */
        SERVER(1),
        /**
         * The databases.
         */
        DBS(2),
        /**
         * The metadata method name.
         */
        METADATA(2),
        /**
         * The database tables.
         */
        TABLES(3),
        /**
         * The table columns.
         */
        COLUMNS(4),
        JOINS(5),
        FILTERS(6),
        ORDERS(7),
        GROUPS(8),
        LIMIT(9),
    ;

        private final int index;
        Position(int index) {
            this.index = index;
        }

    }

    /**
     * Where we log to.
     */
    static final Log LOG = Log.of(URIParser.class);

    /**
     * Note in the log.
     */
    static void note(Object o) {
        LOG.note(o);
    }

    static String at(String uri, Position pos) {
        return uri.split("/")[pos.index];
    }

    static boolean exists(String uri, Position pos) {
        String[] parts = uri.split("/");
        return parts.length > pos.index;
    }

    public static Server getServer(String uri) {
        notNull(uri);
        String name = at(uri,Position.SERVER);
        return Server.uri(URIs.of(name));
    }

    public static Database getDatabase(String uri) {
        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
        Database database = server.databaseName(at(uri,Position.DBS));
        return database;
    }

    public static String getMetaDataMethod(String uri) {
        if (!exists(uri,Position.METADATA)) {
            return "";
        }
        return at(uri,Position.METADATA);
    }

    public static ImmutableList<Database> getDatabases(String uri) {
        if (!exists(uri,Position.DBS)) {
            return ImmutableList.of();
        }
        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
        List<Database> list = Lists.newArrayList();
        for (String databaseName : at(uri,Position.DBS).split("\\+")) {
            list.add(server.databaseName(databaseName));
        }
        return ImmutableList.copyOf(list);
    }

    public static ImmutableList<DBTable> getTables(String uri) {
        if (!exists(uri,Position.TABLES)) {
            return ImmutableList.of();
        }
        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
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
    public static ImmutableList<DBColumn> getColumns(ImmutableList<DBTable> tables, String uri) {
        if (!exists(uri,Position.COLUMNS)) {
            return ImmutableList.of();
        }
        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
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

    public static ImmutableList<AggregateFunction> getFunctions(ImmutableList<DBTable> tables, String uri) {
        if (!exists(uri,Position.COLUMNS)) {
            return ImmutableList.of();
        }
        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
        List<AggregateFunction> list = Lists.newArrayList();
        for (String fullColumnName : at(uri,Position.COLUMNS).split("\\+")) {
            fullColumnName = splitFullColumnName(fullColumnName)[0];
            AggregateFunction function = AggregateFunction.parse(fullColumnName);
            list.add(function);
        }
        return ImmutableList.copyOf(list);
    }

    public static ImmutableList<Join> getJoins(ImmutableList<DBTable> tables,String uri) {
        if (!exists(uri,Position.JOINS)) {
            return ImmutableList.of();
        }
        String joinParts = at(uri,Position.JOINS);
        if (joinParts.length()==0) {
            return ImmutableList.of();
        }
        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
        List<Join> list = Lists.newArrayList();
        for (String fullJoinName : joinParts.split("\\+")) {
            Join join = Join.parse(server,tables,fullJoinName);
            list.add(join);
        }
        return ImmutableList.copyOf(list);
    }

    public static ImmutableList<Filter> getFilters(ImmutableList<DBTable> tables,String uri) {
        if (!exists(uri,Position.FILTERS)) {
            return ImmutableList.of();
        }
        String filterParts = at(uri,Position.FILTERS);
        if (filterParts.length()==0) {
            return ImmutableList.of();
        }

        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
        List<Filter> list = Lists.newArrayList();
        for (String fullFilterName : filterParts.split("\\+")) {
            Filter filter = Filter.parse(server,tables,fullFilterName);
            list.add(filter);
        }
        return ImmutableList.copyOf(list);
    }

    public static ImmutableList<Order> getOrders(ImmutableList<DBTable> tables,String uri) {
        if (!exists(uri,Position.ORDERS)) {
            return ImmutableList.of();
        }
        String orderParts = at(uri,Position.ORDERS);
        if (orderParts.length()==0) {
            return ImmutableList.of();
        }

        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
        List<Order> list = Lists.newArrayList();
        for (String fullOrderName : orderParts.split("\\+")) {
            Order order = Order.parse(server,tables,fullOrderName);
            list.add(order);
        }
        return ImmutableList.copyOf(list);
    }

    public static ImmutableList<Group> getGroups(ImmutableList<DBTable> tables,String uri) {
        if (!exists(uri,Position.GROUPS)) {
            return ImmutableList.of();
        }
        String orderParts = at(uri,Position.GROUPS);
        if (orderParts.length()==0) {
            return ImmutableList.of();
        }

        Server server = Server.uri(URIs.of(at(uri,Position.SERVER)));
        List<Group> list = Lists.newArrayList();
        for (String fullOrderName : orderParts.split("\\+")) {
            Group group = Group.parse(server,tables,fullOrderName);
            list.add(group);
        }
        return ImmutableList.copyOf(list);
    }

    static Limit getLimit(String uri) {
        if (!exists(uri,Position.LIMIT)) {
            return Limit.DEFAULT;
        }
        String[] limitParts = at(uri,Position.LIMIT).split("\\+");
        int  limit = Integer.parseInt(limitParts[0]);
        int offest = Integer.parseInt(limitParts[1]);
        return Limit.limitOffset(limit, offest);
    }

    public static Select getSelect(String uri) {
        note(uri);
        // get everything out of the URL
        ImmutableList<Database>          databases = getDatabases(uri);
        ImmutableList<DBTable>              tables = getTables(uri);
        ImmutableList<DBColumn>            columns = getColumns(tables,uri);
        ImmutableList<AggregateFunction> functions = getFunctions(tables,uri);
        ImmutableList<Join>                  joins = getJoins(tables,uri);
        ImmutableList<Filter>              filters = getFilters(tables,uri);
        ImmutableList<Order>                orders = getOrders(tables,uri);
        ImmutableList<Group>                groups = getGroups(tables,uri);
        Limit                                limit = getLimit(uri);
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
        ImmutableList<Join>         joins,  ImmutableList<Filter>     filters,
        ImmutableList<Order>       orders)
    {
        validateMinimumSize(databases,1);
        validateMinimumSize(tables,1);
    }

    static void validateMinimumSize(List<?> list, int minimum) {
        if (list.size() < minimum) {
            String message = "Must contain at least " + minimum + " elements.";
            throw new IllegalArgumentException(message);
        }
    }


}
