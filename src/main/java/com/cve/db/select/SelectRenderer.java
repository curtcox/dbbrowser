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
/**
 * Renders a {@link Select} as {@link SQL}.
 * @author curt
 */
interface SelectRenderer {

    SQL render(Select select);

    String columns(ImmutableList<DBColumn> columns, ImmutableList<AggregateFunction> functions);

    String tables(ImmutableList<DBTable> tables);

    String where(ImmutableList<Join> joins, ImmutableList<Filter> filters);

    String joins(ImmutableList<Join> joins);

    String filters(ImmutableList<Filter> filters);

    String order(ImmutableList<Order> orders);

    String group(ImmutableList<Group> groups);

    String limit(Limit limit);

}
