package com.cve.io.db.select;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Group;
import com.cve.model.db.Join;
import com.cve.model.db.DBLimit;
import com.cve.model.db.Order;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.io.db.DBResultSetMetaData;
import com.google.common.collect.ImmutableList;

/**
 * For parsing SQL into select statements.
 * Well... right now this doesn't really parse the SQL at all, even though
 * there are several free Java SQL parsers available.  The reason is purely
 * development time.
 * <p>
 * So what does it do, now?  It looks at the results (parsed by the DBMS itself)
 * and tries to determine what the SQL was.
 */
public final class SelectParser {

    public static Select parse(SQL sql, DBResultSetMetaData meta) {
        // Check args
        if (meta.databases.size() < 1) { throw new IllegalArgumentException(); }
        if (meta.tables.size() < 1)    { throw new IllegalArgumentException(); }
        if (meta.columns.size() < 1)   { throw new IllegalArgumentException(); }

        ImmutableList<Database> databases = ImmutableList.copyOf(meta.databases);
        ImmutableList<DBTable> tables = ImmutableList.copyOf(meta.tables);
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(meta.columns);
        ImmutableList<AggregateFunction> functions = ImmutableList.copyOf(meta.functions);
        ImmutableList<Join> joins = ImmutableList.of();
        ImmutableList<DBRowFilter> filters = ImmutableList.of();
        ImmutableList<Order> orders = ImmutableList.of();
        ImmutableList<Group> groups = ImmutableList.of();
        DBLimit limit = DBLimit.DEFAULT;
        Select select = Select.from(databases, tables, columns, functions, joins, filters, orders, groups, limit);
        return select;
    }

}
