package com.cve.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
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

    private final ImmutableList<Database>            databases;
    private final ImmutableList<DBTable>             tables;
    /**
     * There is a 1-to-1 map from columns to functions.
     */
    private final ImmutableList<DBColumn>            columns;

    /**
     * There is a 1-to-1 map from columns to functions.
     */
    private final ImmutableList<AggregateFunction>   functions;
    private final ImmutableList<Join>                joins;
    private final ImmutableList<Filter>              filters;
    private final ImmutableList<Order>               orders;
    private final ImmutableList<Group>               groups;
    private final Limit                              limit;

    private Select(
        ImmutableList<Database> databases,
        ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns,
        ImmutableList<AggregateFunction> functions,
        ImmutableList<Join> joins, ImmutableList<Filter> filters,
        ImmutableList<Order> orders, ImmutableList<Group> groups,
        Limit limit)
    {
        this.databases = notNull(databases);
        this.tables    = notNull(tables);
        this.columns   = notNull(columns);
        this.functions = notNull(functions);
        this.joins     = notNull(joins);
        this.filters   = notNull(filters);
        this.orders    = notNull(orders);
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
        return new Select(databases,tables,columns,functions,joins,filters,orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given join added.
     */
    public Select with(Join join) {
        notNull(join);
        if (joins.contains(join)) {
            return this;
        }
        ImmutableList newTables = with(join.getSource().getTable(),tables);
                      newTables = with(join.getDest().getTable(),newTables);
        return new Select(databases,newTables,columns,functions,with(join,joins),filters,orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given join added.
     */
    public Select with(Filter filter) {
        notNull(filter);
        if (filters.contains(filter)) {
            return this;
        }
        return new Select(databases,tables,columns,functions,joins,with(filter,filters),orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given order added.
     */
    public Select with(Order order) {
        notNull(order);
        if (orders.contains(order)) {
            return this;
        }
        return new Select(databases,tables,columns,functions,joins,filters,with(order,orders),groups,limit);
    }

    /**
     * Return a similar select, but with the given order added.
     */
    public Select with(Group group) {
        notNull(group);
        if (groups.contains(group)) {
            return this;
        }
        return new Select(databases,tables,columns,functions,joins,filters,orders,with(group,groups),limit);
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
        return new Select(databases,tables,append(column,columns),append(function,functions),joins,filters,orders,groups,limit);
    }

    /**
     * Return a query like this one, but just returning a row count.
     */
    public Select count() {
        AggregateFunction count = AggregateFunction.COUNT;
        return new Select(databases,tables,list(DBColumn.ALL),list(count),joins,filters,orders,groups,limit);
    }

    /**
     * Return a similar select, but with the given limit.
     */
    public Select with(Limit limit) {
        return new Select(databases,tables,columns,functions,joins,filters,orders,groups,limit);
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
        return new Select(databases,tables,without(columns,column),ImmutableList.copyOf(list),joins,filters,orders,groups,limit);
    }

    public static Select from(Database database, DBTable table, DBColumn column) {
        return new Select(list(database),list(table),list(column),identityFunctions(1),list(),list(),list(),list(),Limit.DEFAULT);
    }

    public static Select from(Database database, DBTable table, DBColumn column, Filter filter) {
        return new Select(list(database),list(table),list(column),identityFunctions(1),list(),list(filter),list(),list(),Limit.DEFAULT);
    }

    public static Select from(Database database, DBTable t1, DBColumn... columns) {
        return new Select(list(database),list(t1),list(columns),identityFunctions(columns.length),list(),list(),list(),list(),Limit.DEFAULT);
    }

    public static Select from(Database database, DBTable t1, DBTable t2, DBColumn... columns) {
        return new Select(list(database),list(t1,t2),list(columns),identityFunctions(columns.length),list(),list(),list(),list(),Limit.DEFAULT);
    }

    private static ImmutableList<AggregateFunction> identityFunctions(int count) {
        List<AggregateFunction> functions = Lists.newArrayList();
        for (int i=0; i<count; i++) {
            functions.add(AggregateFunction.IDENTITY);
        }
        return ImmutableList.copyOf(functions);
    }

    public ImmutableList<Database>   getDatabases() { return databases;  }
    public ImmutableList<DBTable>       getTables() { return tables;  }
    public ImmutableList<DBColumn>     getColumns() { return columns; }
    public ImmutableList<Join>           getJoins() { return joins;   }
    public ImmutableList<Filter>       getFilters() { return filters;   }
    public ImmutableList<Order>         getOrders() { return orders;  }
    public ImmutableList<Group>         getGroups() { return groups;  }
    public Limit                         getLimit() { return limit;  }
    public ImmutableList<AggregateFunction> getFunctions() { return functions; }

    /**
     * This is just a start...
     */
    public static void validate(Select select) {
        if (select.getColumns().size()!=select.getFunctions().size()) {
            throw new IllegalArgumentException("Column count!= function count");
        }
        if (select.getDatabases().size()<1) {
            throw new IllegalArgumentException("No DB specified");
        }
        if (select.getTables().size()<1) {
            throw new IllegalArgumentException("No tables specified");
        }
        if (select.getColumns().size()<1) {
            throw new IllegalArgumentException("No columns specified");
        }
        for (Join join : select.getJoins()) {
            if (!select.getTables().contains(join.getDest().getTable())) {
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
}
