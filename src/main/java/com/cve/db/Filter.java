package com.cve.db;

import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * A condition that rejects some rows based upon the value of a particular column.
 * A filter corresponds to a {@link SQL} where clause that only involves one
 * {@link Column}.
 * See also {@link Join}
 */
@Immutable
public final class Filter {

    private final DBColumn column;
    private final Value value;

    private Filter(DBColumn column, Value value) {
        this.column = notNull(column);
        this.value  = notNull(value);
    }
    
    public static Filter of(DBColumn column, Value value) {
        return new Filter(column,value);
    }

    public static Filter parse(Server server, ImmutableList<DBTable> tables, String fullFilterName) {
        notNull(server);
        notNull(fullFilterName);
        String[]  nameParts = fullFilterName.split("\\=");
        if (nameParts.length!=2) {
            String message = fullFilterName + " is not of the form database.table.column=value";
            throw new IllegalArgumentException(message);
        }
        DBColumn       column = DBColumn.parse(server,tables,nameParts[0]);
        Value         value = Value.of(nameParts[1]);
        Filter       filter = Filter.of(column, value);
        return filter;
    }

    public DBColumn getColumn() { return column; }
    public Value  getValue()  { return value; }

    @Override
    public int hashCode() { return column.hashCode() ^ value.hashCode(); }

    @Override
    public boolean equals(Object o) {
        Filter other = (Filter) o;
        return column.equals(other.column) && value.equals(other.value);
    }
}
