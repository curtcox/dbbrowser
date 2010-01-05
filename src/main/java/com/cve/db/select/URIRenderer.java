package com.cve.db.select;

import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Filter;
import com.cve.db.Join;
import com.cve.db.Limit;
import com.cve.db.Order;
import com.cve.db.DBServer;
import com.cve.db.Select;
import com.cve.db.DBTable;
import com.cve.db.Group;
import com.cve.util.URIs;
import com.cve.web.Search;
import com.cve.web.db.DBURICodec;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;
import static com.cve.util.Check.notNull;
/**
 * For rendering a {@link Select} as a {@link URI}.
 * Every Select used in the application should be renderable as a URI.
<pre>
  SELECT column1, column2
  FROM table1, table2
  JOIN table1.id = table2.f_id
  WHERE table1.val_col = "active"
</pre>
<pre>
  /server/database/table1+table2/column1+column2/table1.id=table2.f_id/table1.val_col=active/
</pre>
 * Experience has shown that without some form of expression, these URIs will
 * occasionally become too big for Grizzly to handle.  Since this also makes
 * them too big to be cut-and-paste friendly, some form of compression is in
 * order.
 * <p>
 * The bulk of these big URI tends to be a long list of columns.  The following
 * compression scheme will keep the URIs "human readable", while compressing
 * them by about two thirds.
 * <p>
 * <ol>
 * <li> Column names for the first listed table don't include any prefix.
 * They only include the column name itself.
 * <li> Column names for other tables use the position number of the table to
 * generate a qualifying prefix, instead of the table name.  The second listed
 * table corresponds to "0", the third to "1", etc..
 * <li> No period is used to separate a numeric column prefix from the column
 * name.
 * </ol>
 * @author curt
 */
public final class URIRenderer {

    /**
     * Render the given select statement as a URI.
     */
    public static URI render(Select select, Search search) {
        notNull(select);
        validate(select);
        StringBuilder out = new StringBuilder();
        DBServer server = select.server;
        out.append(DBURICodec.encode(search));
        out.append(server.uri + "/");
        ImmutableList<DBTable> tables = select.tables;
        out.append(renderDatabases(select.databases) + "/");
        out.append(renderTables   (tables)    + "/");
        out.append(renderColumns  (tables,select.columns)   + "/");
        final boolean hasJoin   = select.joins.size()   > 0;
        final boolean hasFilter = select.filters.size() > 0;
        final boolean hasOrder  = select.orders.size()  > 0;
        final boolean hasGroups = select.groups.size()  > 0;
        final boolean hasLimit  = !select.limit.equals(Limit.DEFAULT);
        if (hasJoin || hasFilter || hasOrder || hasGroups || hasLimit) {
            out.append(renderJoins(tables,select.joins)   + "/");
        }
        if (hasFilter || hasOrder || hasGroups || hasLimit) {
            out.append(renderFilters(tables,select.filters)   + "/");
        }
        if (hasOrder || hasGroups || hasLimit) {
            out.append(renderOrders(tables,select.orders)     + "/");
        }
        if (hasGroups || hasLimit) {
            out.append(renderGroups(tables,select.groups)     + "/");
        }
        if (hasLimit) {
            out.append(renderLimit(select.limit)       + "/");
        }
        return URIs.of(out.toString());
    }

    /**
     * Validate the given select statement.
     */
    static void validate(Select select) {
        Select.validate(select);
    }

    static String renderDatabases(ImmutableList<Database> databases) {
        List<String> list = Lists.newArrayList();
        for (Database database : databases) {
            list.add(database.name);
        }
        return separated(list);
    }

    static String renderTables(ImmutableList<DBTable> tables) {
        List<String> list = Lists.newArrayList();
        for (DBTable table : tables) {
            list.add(table.fullName());
        }
        return separated(list);
    }

    static String shortName(ImmutableList<DBTable> tables, DBColumn column) {
        if (tables.get(0).equals(column.table)) {
            return column.name;
        }
        for (int i=1; i<tables.size(); i++) {
            if (tables.get(i).equals(column.table)) {
                return (i-1) + column.name;
            }
        }
        String message = column + " not in " + tables;
        throw new IllegalArgumentException(message);
    }

    static String renderColumns(ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns) {
        List<String> list = Lists.newArrayList();
        for (DBColumn column : columns) {
            list.add(shortName(tables,column));
        }
        return separated(list);
    }

    static String renderJoins(ImmutableList<DBTable> tables, ImmutableList<Join> joins) {
        List<String> list = Lists.newArrayList();
        for (Join join : joins) {
            list.add(shortName(tables,join.source) + "=" + shortName(tables,join.dest));
        }
        return separated(list);
    }

    static String renderFilters(ImmutableList<DBTable> tables, ImmutableList<Filter> filters) {
        List<String> list = Lists.newArrayList();
        for (Filter filter : filters) {
            list.add(shortName(tables,filter.column) + "=" + filter.value.encode());
        }
        return separated(list);
    }

    static String renderOrders(ImmutableList<DBTable> tables, ImmutableList<Order> orders) {
        List<String> list = Lists.newArrayList();
        for (Order order : orders) {
            list.add(shortName(tables,order.column) + "=" + order.direction.toString());
        }
        return separated(list);
    }

    static String renderGroups(ImmutableList<DBTable> tables, ImmutableList<Group> groups) {
        List<String> list = Lists.newArrayList();
        for (Group group : groups) {
            list.add(shortName(tables,group.column));
        }
        return separated(list);
    }

    static String renderLimit(Limit limit) {
        return limit.limit + "+" + limit.offset;
    }

    static String separated(List<String> list) {
        StringBuilder out = new StringBuilder();
        for (int i=0; i<list.size(); i++) {
            out.append(list.get(i));
            if (i<list.size() - 1) {
                out.append("+");
            }
        }
        return out.toString();
    }
}
