package com.cve.io.db;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.model.db.DBTable;
import com.cve.model.db.Join;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Group;
import com.cve.model.db.DBLimit;
import com.cve.model.db.Order;
import com.cve.web.core.Search;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import static com.cve.util.Check.notNull;
/**
 * Renders a {@link Select} as {@link SQL}.
 * This class is meant to be fairly easy to extend, but you can always just
 * implement SelectRenderer from scratch.
 * @author curt
 */
public class SimpleSelectRenderer implements SelectRenderer {

    // Constants we use in the generated SQL
    private static final String AND      = " AND ";
    private static final String OR       = " OR ";
    private static final String LIKE     = " LIKE ";
    private static final String FROM     = " FROM ";
    private static final String WHERE    = " WHERE ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String GROUP_BY = " GROUP BY ";
    private static final String LIMIT    = " LIMIT ";
    private static final String OFFSET   = " OFFSET ";
    
    @Override
    public SQL render(Select select, Search search) {
        notNull(select);
        StringBuilder out = new StringBuilder();
        out.append("SELECT ");
        out.append(columns(select.columns,select.functions));
        out.append(FROM);
        out.append(tables(select.tables));
        out.append(where(select.joins,select.filters,search,select.columns));
        out.append(order(select.orders));
        out.append(group(select.groups));
        out.append(limit(select.limit));
        return SQL.of(out.toString());
    }

    @Override
    public SQL renderCount(Select select, Search search) {
        notNull(select);
        StringBuilder out = new StringBuilder();
        out.append("SELECT ");
        out.append(" count(*) ");
        out.append(FROM);
        out.append(tables(select.tables));
        out.append(where(select.joins,select.filters,search,select.columns));
        // Order clauses don't change row counts, but some database engines
        // require any columns used in the order clauses to be included in
        // those selected.  Since we're only selecting count(*), that's no
        // columns.  So we need to and are free to drop the order clauses.
        out.append(group(select.groups));
        // We should only be getting one value back (the count), but some
        // DBMS's don't like putting a limit on the SQL.
        // So don't add the limit.
        return SQL.of(out.toString());
    }

    public String columns(ImmutableList<DBColumn> columns, ImmutableList<AggregateFunction> functions) {
        List<String> list = Lists.newArrayList();
        for (int i=0; i<columns.size(); i++) {
            DBColumn            column = columns.get(i);
            AggregateFunction function = functions.get(i);
            if (function.equals(AggregateFunction.IDENTITY)) {
                list.add(fullName(column));
            } else {
                list.add(function.toString() + "(" + fullName(column) + ")");
            }
        }
        return separated(list,",");
    }

    public String tables(ImmutableList<DBTable> tables) {
        List<String> list = Lists.newArrayList();
        for (DBTable table : tables) {
            list.add(fullName(table));
        }
        return separated(list,",");
    }

    public String where(
        ImmutableList<Join> joins, ImmutableList<DBRowFilter> filters,
        Search search, ImmutableList<DBColumn> columns)
    {
        boolean   hasJoins =   joins.size() > 0;
        boolean hasFilters = filters.size() > 0;
        boolean  hasSearch =  !search.isEmpty();
        if (!hasJoins && !hasFilters && !hasSearch) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        out.append(WHERE);
        out.append(joins(joins));
        if (hasJoins && hasFilters) {
            out.append(AND);
        }
        out.append(filters(filters));
        if ((hasJoins || hasFilters) && hasSearch) {
            out.append(AND);
        }
        out.append(search(search,columns));
        return out.toString();
    }

    public String joins(ImmutableList<Join> joins) {
        List<String> list = Lists.newArrayList();
        for (Join join : joins) {
            list.add(fullName(join.source) + "=" + fullName(join.dest));
        }
        return separated(list,AND);
    }

    public String filters(ImmutableList<DBRowFilter> filters) {
        List<String> list = Lists.newArrayList();
        for (DBRowFilter filter : filters) {
            list.add(fullName(filter.column) + "=" + singleQuote(filter.value.toString()));
        }
        return separated(list,AND);
    }

    public String search(Search search, ImmutableList<DBColumn> columns) {
        if (search.isEmpty()) {
            return "";
        }
        List<String> list = Lists.newArrayList();
        for (DBColumn column : columns) {
            list.add(lower(fullName(column)) + LIKE + lower(singleQuote("%" + search.target + "%")));
        }
        return "(" + separated(list,OR) + ")";
    }

    public String order(ImmutableList<Order> orders) {
        if (orders.size() < 1) {
            return "";
        }
        List<String> list = Lists.newArrayList();
        for (Order order : orders) {
            list.add(fullName(order.column) + " " + order.direction.toString());
        }
        return ORDER_BY + separated(list,",");
    }

    public String group(ImmutableList<Group> groups) {
        if (groups.size() < 1) {
            return "";
        }
        List<String> list = Lists.newArrayList();
        for (Group group : groups) {
            list.add(fullName(group.column) + " ");
        }
        return GROUP_BY + separated(list,AND);
    }

    public String limit(DBLimit limit) {
        // Use limit + 1, so we can see if there is more data to get,
        // without the risk of accidentally getting way too much.
        return LIMIT + ( limit.limit + 1 ) + OFFSET + limit.offset;
    }

    public String fullName(DBColumn column) {
        return column.fullName();
    }

    public String fullName(DBTable table) {
        return table.fullName();
    }

    static String separated(List<String> list, String separator) {
        StringBuilder out = new StringBuilder();
        for (int i=0; i<list.size(); i++) {
            out.append(list.get(i));
            if (i<list.size() - 1) {
                out.append(separator);
            }
        }
        return out.toString();
    }

    static String singleQuote(String value) {
        return "'" + value + "'";
    }

    static String lower(String value) {
        return "lower(" + value + ")";
    }

}
