package com.cve.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A logical representation of a select statement against a {@link Datatbase}
 * {@link Server}.  A Select can be rendered as a {@link URI}, rendered as
 * {@link SQL}, or run to produce a {@link SelectResults}.
 * <p>
 * Instances are immutable.
 * @author Curt
 */
@Immutable
public final class Select {

    /**
     * The server this select will execute on.
     */
    public final DBServer server;

    /**
     * The databases this select uses.
     */
    public final ImmutableList<Database>            databases;

    /**
     * The tables this select uses.
     */
    public final ImmutableList<DBTable>             tables;
    /**
     * There is a 1-to-1 map from columns to functions.
     */
    public final ImmutableList<DBColumn>            columns;

    /**
     * There is a 1-to-1 map from columns to functions.
     */
    public final ImmutableList<AggregateFunction>   functions;
    public final ImmutableList<Join>                joins;
    public final ImmutableList<Filter>              filters;
    public final ImmutableList<Order>               orders;
    public final ImmutableList<Group>               groups;
    public final Limit                              limit;

    private Select(
        DBServer server,
        ImmutableList<Database> databases,
        ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns,
        ImmutableList<AggregateFunction> functions,
        ImmutableList<Join> joins, ImmutableList<Filter> filters,
        ImmutableList<Order> orders, ImmutableList<Group> groups,
        Limit limit)
    {
        this.server    = notNull(server);
        this.databases = notNull(databases);
        this.tables    = notNull(tables);
        this.columns   = notNull(columns);
        this.functions = notNull(functions);
        this.joins     = notNull(joins);
        this.filters   = notNull(filters);
        this.orders    = Order.normalize(notNull(orders));
        this.groups    = notNull(groups);
        this.limit     = notNull(limit);
        if (columns.size()!=functions.size()) {
            throw new IllegalArgumentException(columns.size()+ "!=" + functions.size());
        }
    }

    public static Select from(
        ImmutableList<Database> databases,
        ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns,
        ImmutableList<AggregateFunction> functions,
        ImmutableList<Join> joins, ImmutableList<Filter> filters,
        ImmutableList<Order> orders,  ImmutableList<Group> groups,
        Limit limit)
    {
        DBServer server = databases.get(0).server;
        return new Select(server,databases,tables,columns,functions,joins,filters,orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given join added.
     */
    public Select with(Join join) {
        notNull(join);
        if (joins.contains(join)) {
            return this;
        }
        ImmutableList newTables = with(join.source.table,tables);
                      newTables = with(join.dest.table,newTables);
        return new Select(server,databases,newTables,columns,functions,with(join,joins),filters,orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given join added.
     */
    public Select with(Filter filter) {
        notNull(filter);
        if (filters.contains(filter)) {
            return this;
        }
        return new Select(server,databases,tables,columns,functions,joins,with(filter,filters),orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given order added.
     */
    public Select with(Order order) {
        notNull(order);
        if (orders.contains(order)) {
            return this;
        }
        return new Select(server,databases,tables,columns,functions,joins,filters,withOrder(order,orders),groups,limit);
    }

    /**
     * Return a similar select, but with the given order added.
     */
    public Select with(Group group) {
        notNull(group);
        if (groups.contains(group)) {
            return this;
        }
        return new Select(server,databases,tables,columns,functions,joins,filters,orders,with(group,groups),limit);
    }

    /**
     * Return a similar select, but with the given column added.
     */
    public Select with(DBColumn column) {
        return with(column,AggregateFunction.IDENTITY);
    }

    /**
     * Return a similar select, but with the given column added.
     */
    public Select with(DBColumn column, AggregateFunction function) {
        notNull(column);
        if (columns.contains(column) && functions.get(columns.indexOf(column)).equals(function)) {
            return this;
        }
        return new Select(server,databases,tables,append(column,columns),append(function,functions),joins,filters,orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given limit.
     */
    public Select with(Limit limit) {
        return new Select(server,databases,tables,columns,functions,joins,filters,orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given column subtracted.
     */
    public Select without(DBColumn column) {
        notNull(column);
        if (!columns.contains(column)) {
            return this;
        }
        List<AggregateFunction> list = Lists.newArrayList();
        list.addAll(functions);
        list.remove(columns.indexOf(column));
        return new Select(server,databases,tables,without(columns,column),ImmutableList.copyOf(list),joins,filters,orders,groups,limit);
    }

    /**
     * Return a similar select, but using these columns instead.
     */
    public Select with(List<DBColumn> newColumns) {
        ImmutableList<DBColumn> copy = ImmutableList.copyOf(newColumns);
        return new Select(server,databases,tables,copy,functions,joins,filters,orders,groups,limit);
    }

    public static Select from(Database database, DBTable table, DBColumn column) {
        DBServer server = database.server;
        return new Select(server,list(database),list(table),list(column),identityFunctions(1),list(),list(),list(),list(),Limit.DEFAULT);
    }

    public static Select from(Database database, DBTable table, DBColumn column, Filter filter) {
        DBServer server = database.server;
        return new Select(server,list(database),list(table),list(column),identityFunctions(1),list(),list(filter),list(),list(),Limit.DEFAULT);
    }

    public static Select from(Database database, DBTable t1, DBColumn... columns) {
        DBServer server = database.server;
        return new Select(server,list(database),list(t1),list(columns),identityFunctions(columns.length),list(),list(),list(),list(),Limit.DEFAULT);
    }

    public static Select from(Database database, DBTable t1, DBTable t2, DBColumn... columns) {
        DBServer server = database.server;
        return new Select(server,list(database),list(t1,t2),list(columns),identityFunctions(columns.length),list(),list(),list(),list(),Limit.DEFAULT);
    }

    private static ImmutableList<AggregateFunction> identityFunctions(int count) {
        List<AggregateFunction> functions = Lists.newArrayList();
        for (int i=0; i<count; i++) {
            functions.add(AggregateFunction.IDENTITY);
        }
        return ImmutableList.copyOf(functions);
    }


    /**
     * This is just a start...
     */
    public static void validate(Select select) {
        if (select.columns.size()!=select.functions.size()) {
            throw new IllegalArgumentException("Column count!= function count");
        }
        if (select.databases.size()<1) {
            throw new IllegalArgumentException("No DB specified");
        }
        if (select.tables.size()<1) {
            throw new IllegalArgumentException("No tables specified");
        }
        if (select.columns.size()<1) {
            throw new IllegalArgumentException("No columns specified");
        }
        for (Join join : select.joins) {
            if (!select.tables.contains(join.dest.table)) {
                throw new IllegalArgumentException("Join tables must be included in tables");
            }
        }
    }


    private static <T> ImmutableList<T> list(T one) {
        return ImmutableList.of(one);
    }

    private static <T> ImmutableList<T> list(T... items) {
        return ImmutableList.of(items);
    }

    private static ImmutableList list() {
        return ImmutableList.of();
    }

    private static <T> ImmutableList<T> without(ImmutableList<T> old, T t) {
        List<T> list = Lists.newArrayList();
        list.addAll(old);
        list.remove(t);
        return ImmutableList.copyOf(list);
    }

    private static <T> ImmutableList<T> with(T t, ImmutableList<T> items) {
        if (items.contains(t)) {
            return items;
        }
        List<T> list = Lists.newArrayList();
        list.addAll(items);
        list.add(t);
        return ImmutableList.copyOf(list);
    }

    private static ImmutableList<Order> withOrder(Order added, ImmutableList<Order> orders) {
        if (orders.contains(added)) {
            return orders;
        }
        List<Order> list = Lists.newArrayList();
        list.addAll(orders);
        for (Iterator<Order> i = list.iterator(); i.hasNext(); ) {
            Order order = i.next();
            if (order.column.equals(added.column)) {
                i.remove();  // take it out
            }
        }
        if (added.direction!=Order.Direction.NONE) {
            list.add(added);
        }
        return ImmutableList.copyOf(list);
    }

    private static <T> ImmutableList<T> append(T t, ImmutableList<T> items) {
        List<T> list = Lists.newArrayList();
        list.addAll(items);
        list.add(t);
        return ImmutableList.copyOf(list);
    }


    @Override
    public String toString() {
        return " database = "  + databases +
               " tables = "    + tables +
               " columns = "   + columns +
               " functions = " + functions +
               " joins = "     + joins +
               " filters = "   + filters +
               " orders ="     + orders +
               " groups = "    + groups +
               " limit = "     + limit;
    }

    @Override
    public int hashCode() {
        return server.hashCode() ^
               databases.hashCode() ^
               tables.hashCode() ^
               columns.hashCode() ^
               functions.hashCode() ^
               joins.hashCode() ^
               filters.hashCode() ^
               orders.hashCode() ^
               groups.hashCode() ^
               limit.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        Select other = (Select) o;
        return server.equals(other.server) &&
               databases.equals(other.databases) &&
               tables.equals(other.tables) &&
               columns.equals(other.columns) &&
               functions.equals(other.functions) &&
               joins.equals(other.joins) &&
               filters.equals(other.filters) &&
               orders.equals(other.orders) &&
               groups.equals(other.groups) &&
               limit.equals(other.limit);
    }

}
