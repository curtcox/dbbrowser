package com.cve.model.db;

import com.cve.log.Log;
import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;
import org.h2.table.Column;
import static com.cve.util.Check.notNull;

/**
 * A condition that rejects some rows based upon the value of a particular column.
 * A filter corresponds to a {@link SQL} where clause that only involves one
 * {@link Column}.
 * See also {@link Join}
 */
@Immutable
public final class DBRowFilter {

    /**
     * The column this filter filters.
     */
    public final DBColumn column;

    /**
     * The value this filter passes.
     */
    public final DBValue value;

    public final Log log;

    /**
     * Use the factory.
     */
    private DBRowFilter(DBColumn column, DBValue value) {
        this.column = notNull(column);
        this.value  = notNull(value);
        this.log = notNull(column.log);
    }

    /**
     * Return a filter for the given column and value.
     */
    public static DBRowFilter of(DBColumn column, DBValue value) {
        return new DBRowFilter(column,value);
    }

    /**
     * Parses a filter that has previously been rendered as a URL fragment.
     * See toURrlFragment.
     */
    public static DBRowFilter parse(DBServer server, ImmutableList<DBTable> tables, String fullFilterName) {
        Log log  = server.log;
        log.args(server,tables,fullFilterName);
        notNull(server);
        notNull(fullFilterName);
        String[]  nameParts = fullFilterName.split("\\=");
        if (nameParts.length!=2) {
            String message = fullFilterName + " is not of the form database.table.column=value";
            throw new IllegalArgumentException(message);
        }
        DBColumn       column = DBColumn.parse(server,tables,nameParts[0]);
        String         string = nameParts[1];
        DBValue         value = DBValue.decode(string);
        DBRowFilter       filter = DBRowFilter.of(column, value);
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
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        DBRowFilter other = (DBRowFilter) o;
        return column.equals(other.column) && value.equals(other.value);
    }

    @Override
    public String toString() {
        return "column=" + column + " value=" + value;
    }
}
