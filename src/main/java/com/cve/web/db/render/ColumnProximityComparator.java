package com.cve.web.db.render;

import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.DBTable;
import java.util.Comparator;
import static com.cve.util.Check.notNull;

/**
 * Ranks columns based upon how close they are to the given column.
 * Columns in the same table are closest.  Then those in the same database.
 * Then by alphabetical order.
 * @author curt
 */
public final class ColumnProximityComparator implements Comparator<DBColumn> {

    private final DBTable table;
    private final Database database;

    ColumnProximityComparator(DBColumn column) {
        notNull(column);
        this.table    = column.table;
        this.database = table.database;
    }

    @Override
    public int compare(DBColumn c1, DBColumn c2) {
        DBTable t1 = c1.table;
        DBTable t2 = c2.table;
        if (t1.equals(table) && !t2.equals(table)) {
            return -1;
        }
        if (!t1.equals(table) && t2.equals(table)) {
            return +1;
        }
        Database d1 = t1.database;
        Database d2 = t2.database;
        if (d1.equals(database) && !d2.equals(database)) {
            return -1;
        }
        if (!d1.equals(database) && d2.equals(database)) {
            return +1;
        }
        String n1 = c1.name;
        String n2 = c2.name;
        return n1.compareTo(n2);
    }
}
