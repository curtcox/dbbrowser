package com.cve.db;

import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;
import org.h2.table.Column;
import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * A condition that rejects some rows based upon the value of a particular column.
 * A filter corresponds to a {@link SQL} where clause that only involves one
 * {@link Column}.
 * See also {@link Join}
 */
@Immutable
public final class Filter {

    /**
     * The column this filter filters.
     */
    public final DBColumn column;

    /**
     * The value this filter passes.
     */
    public final Value value;

    /**
     * Use the factory.
     */
    private Filter(DBColumn column, Value value) {
        this.column = notNull(column);
        this.value  = notNull(value);
    }

    /**
     * Return a filter for the given column and value.
     */
    public static Filter of(DBColumn column, Value value) {
        return new Filter(column,value);
    }

    /**
     * Parses a filter that has previously been rendered as a URL fragment.
     * See toURrlFragment.
     */
    public static Filter parse(Server server, ImmutableList<DBTable> tables, String fullFilterName) {
        args(server,tables,fullFilterName);
        notNull(server);
        notNull(fullFilterName);
        String[]  nameParts = fullFilterName.split("\\=");
        if (nameParts.length!=2) {
            String message = fullFilterName + " is not of the form database.table.column=value";
            throw new IllegalArgumentException(message);
        }
        DBColumn       column = DBColumn.parse(server,tables,nameParts[0]);
        String         string = nameParts[1];
        Value         value = Value.decode(string);
        Filter       filter = Filter.of(column, value);
        return filter;
    }

    /**
     * Constructs a URL fragment that will be used in a query string or in
     * the filters section of a select URL.
     * See parse.
     */
    public String toUrlFragment() {
        return column.fullName() + "=" + value.encode();
    }

    @Override
    public int hashCode() { return column.hashCode() ^ value.hashCode(); }

    @Override
    public boolean equals(Object o) {
        Filter other = (Filter) o;
        return column.equals(other.column) && value.equals(other.value);
    }

    @Override
    public String toString() {
        return "column=" + column + " value=" + value;
    }
}