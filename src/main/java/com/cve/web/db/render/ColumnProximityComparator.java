package com.cve.web.db.render;

import com.cve.model.db.DBColumn;
import static com.cve.model.db.DBColumn.Keyness.*;
import com.cve.model.db.Database;
import com.cve.model.db.DBTable;
import java.util.Comparator;
import static com.cve.util.Check.notNull;

/**
 * Ranks columns based upon how close they are to the given column.
 * Columns in the same table are closest.  Then those in the same database.
 * Then by alphabetical order.
 * @author curt
 */
public final class ColumnProximityComparator implements Comparator<DBColumn> {

    /**
     * The table we are joining to.
     */
    private final DBTable table;

    /**
     * The database we are joining to.
     */
    private final Database database;

    /**
     * Create a new comparitor, given the column we are joining to.
     * @param column
     */
    ColumnProximityComparator(DBColumn column) {
        notNull(column);
        this.table    = column.table;
        this.database = table.database;
    }

    @Override
    public int compare(DBColumn c1, DBColumn c2) {
        // if c1 is a key, but c2 isn't, c1 is better
        if (c1.keyness!=NONE && c2.keyness==NONE) {
            return -1;
        }
        // if c2 is a key, but c1 isn't, c2 is better
        if (c1.keyness==NONE && c2.keyness!=NONE) {
            return +1;
        }

        // Anything in the same table as the target column sorts higher
        DBTable t1 = c1.table;
        DBTable t2 = c2.table;

        // if c1 is the same table, but c2 isn't c1 is better
        if (t1.equals(table) && !t2.equals(table)) {
            return -1;
        }
        // if c2 is the same table, but c1 isn't c2 is better
        if (!t1.equals(table) && t2.equals(table)) {
            return +1;
        }

        // Anything in the same database as the target column sorts higher
        Database d1 = t1.database;
        Database d2 = t2.database;
        // if c1 is the same database, but c2 isn't c1 is better
        if (d1.equals(database) && !d2.equals(database)) {
            return -1;
        }
        // if c2 is the same database, but c1 isn't c2 is better
        if (!d1.equals(database) && d2.equals(database)) {
            return +1;
        }

        // If all else is equal, compare by name
        String n1 = c1.name;
        String n2 = c2.name;
        return n1.compareTo(n2);
    }
}
