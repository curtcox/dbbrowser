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
        out.append(limit(select.limit));
        out.append(columns(select.columns,select.functions));
        out.append(FROM);
        out.append(tables(select.tables));
        out.append(where(select.joins,select.filters));
        out.append(order(select.orders));
        out.append(group(select.groups));
        return SQL.of(out.toString());
    }
    
    /**
     * SELECT TOP 10 START AT 20 * FROM T
     */
    @Override
    public String limit(Limit limit) {
        // Use limit + 1, so we can see if there is more data to get,
        // without the risk of accidentally getting way too much.
        return TOP + ( limit.limit + 1 ) + " ";
    }

    @Override
    public String fullName(DBColumn column) {
        if (column.equals(DBColumn.ALL)) {
            return DBColumn.ALL.fullName();
        }
        return fullName(column.table) + "." + column.name;
    }

    @Override
    public String fullName(DBTable table) {
        return table.database.name + ".dbo." + table.name;
    }

}
