package com.cve.db.select;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Limit;
import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.util.Check;

/**
 *
 * @author curt
 */
final class MsSqlSelectRenderer extends SimpleSelectRenderer {

    private static final String TOP       = " TOP ";
    private static final String START_AT  = " START AT ";
    private static final String FROM      = " FROM ";
    
    private MsSqlSelectRenderer() {}

    static SelectRenderer of() {
        return new MsSqlSelectRenderer();
    }

    @Override
    public SQL render(Select select) {
        Check.notNull(select);
        StringBuilder out = new StringBuilder();
        out.append("SELECT ");
        out.append(limit(select.getLimit()));
        out.append(columns(select.getColumns(),select.getFunctions()));
        out.append(FROM);
        out.append(tables(select.getTables()));
        out.append(where(select.getJoins(),select.getFilters()));
        out.append(order(select.getOrders()));
        out.append(group(select.getGroups()));
        return SQL.of(out.toString());
    }
    
    /**
     * SELECT TOP 10 START AT 20 * FROM T
     */
    @Override
    public String limit(Limit limit) {
        // Use limit + 1, so we can see if there is more data to get,
        // without the risk of accidentally getting way too much.
        return TOP + ( limit.getLimit() + 1 ) + " ";
    }

    @Override
    public String fullName(DBColumn column) {
        if (column.equals(DBColumn.ALL)) {
            return DBColumn.ALL.fullName();
        }
        return fullName(column.getTable()) + "." + column.getName();
    }

    @Override
    public String fullName(DBTable table) {
        return table.getDatabase().getName() + ".dbo." + table.getName();
    }

}
