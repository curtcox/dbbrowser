package com.cve.db.select;

import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.db.DBTable;
import com.cve.db.Join;
import com.cve.db.Filter;
import com.cve.db.Group;
import com.cve.db.Limit;
import com.cve.db.Order;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import static com.cve.util.Check.notNull;
/**
 * Renders a {@link Select} as {@link SQL}.
 * @author curt
 */
class SimpleSelectRenderer implements SelectRenderer {

    private static final String AND      = " AND ";
    private static final String FROM     = " FROM ";
    private static final String WHERE    = " WHERE ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String GROUP_BY = " GROUP BY ";
    private static final String LIMIT    = " LIMIT ";
    private static final String OFFSET   = " OFFSET ";
    
    public SQL render(Select select) {
        notNull(select);
        StringBuilder out = new StringBuilder();
        out.append("SELECT ");
        out.append(columns(select.columns,select.functions));
        out.append(FROM);
        out.append(tables(select.tables));
        out.append(where(select.joins,select.filters));
        out.append(order(select.orders));
        out.append(group(select.groups));
        out.append(limit(select.limit));
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

    public String where(ImmutableList<Join> joins, ImmutableList<Filter> filters) {
        boolean   hasJoins =   joins.size() > 0;
        boolean hasFilters = filters.size() > 0;
        if (!hasJoins && !hasFilters) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        out.append(WHERE);
        out.append(joins(joins));
        if (hasJoins && hasFilters) {
            out.append(AND);
        }
        out.append(filters(filters));
        return out.toString();
    }

    public String joins(ImmutableList<Join> joins) {
        List<String> list = Lists.newArrayList();
        for (Join join : joins) {
            list.add(fullName(join.source) + "=" + fullName(join.dest));
        }
        return separated(list,AND);
    }

    public String filters(ImmutableList<Filter> filters) {
        List<String> list = Lists.newArrayList();
        for (Filter filter : filters) {
            list.add(fullName(filter.column) + "=" + singleQuote(filter.value.toString()));
        }
        return separated(list,AND);
    }

    public String order(ImmutableList<Order> orders) {
        if (orders.size() < 1) {
            return "";
        }
        List<String> list = Lists.newArrayList();
        for (Order order : orders) {
            list.add(fullName(order.column) + " " + order.direction.toString());
        }
        return ORDER_BY + separated(list,AND);
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

    public String limit(Limit limit) {
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
}
