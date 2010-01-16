package com.cve.model.db;

import com.cve.web.Model;
import com.cve.web.Search;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * The results of running a {@link Select} against a {@link Server}.
 * This consists of the select itself, the {@link ResultSet} returned,
 * and {@link Hints} for producing future SelectS.
 * @author Curt
 */
@Immutable
public final class SelectResults implements Model {

    /**
     * The type of results these are -- either normal data, or a column value
     * distribution.
     */
    public enum Type {

        /**
         * Columns from tables -- perhaps joined, filtered, and sorted.
         */
        NORMAL_DATA,

        /**
         * Column value distribution data.
         * This indicates a result set that shows the distribution of data in
         * a single column.  The user gets to this data by clicking on a
         * column title.  It is worth distinguishing, because it will be
         * displayed differently than normal data.
         */
        COLUMN_VALUE_DISTRIBUTION
    }

    /**
     * The server these results are from.
     */
    public final DBServer server;

    /**
     * The select used to generate the results
     */
    public final Select select;

    /**
     * The search -- likely empty -- used to generate the results.
     */
    public final Search search;

    /**
     * The results of the select
     */
    public final DBResultSet resultSet;

    /**
     * Hints about how the results should be displayed.
     */
    public final Hints hints;

    /**
     * The total number of rows that would be in the result set if no limit
     * were used.
     */
    public final long count;

    /**
     * True if there are more rows available for the select, after the ones
     * in the result set.
     */
    public final boolean hasMore;

    /**
     * The type of results these are.
     */
    public final Type type;


    private SelectResults(DBServer server,Type type, Select select, Search search, DBResultSet resultSet, Hints hints, long count, boolean hasMore) {
        this.server    = notNull(server);
        this.type      = notNull(type);
        this.select    = notNull(select);
        this.search    = notNull(search);
        this.resultSet = notNull(resultSet);
        this.hints     = notNull(hints);
        this.count     = count;
        this.hasMore   = hasMore;
    }

    static DBServer check(Select select, DBResultSet resultSet) {
        DBServer a = select.databases.get(0).server;
        DBServer b = resultSet.databases.get(0).server;
        if (a.equals(b)) {
            return a;
        }
        throw new IllegalArgumentException(a + "!=" + b);
    }

    public static SelectResults selectResultsHintsMore(
        Select select, DBResultSet resultSet, Hints hints, boolean hasMore)
    {
        DBServer server = check(select,resultSet);
        Search search = Search.EMPTY;
        return new SelectResults(server,Type.NORMAL_DATA,select,search,resultSet,hints,100,hasMore);
    }

    public static SelectResults selectResultsHintsCountMore(
        Select select, DBResultSet resultSet, Hints hints, int count, boolean hasMore)
    {
        DBServer server = check(select,resultSet);
        Search search = Search.EMPTY;
        return new SelectResults(server,Type.NORMAL_DATA,select,search,resultSet,hints,count,hasMore);
    }

    public static SelectResults typeSelectSearchResultsHintsCountMore(
        Type type, Select select, Search search, DBResultSet resultSet, Hints hints, long count, boolean hasMore)
    {
        DBServer server = check(select,resultSet);
        return new SelectResults(server,type,select,search,resultSet,hints,count,hasMore);
    }

    @Override
    public int hashCode() {
        return select.hashCode() ^ resultSet.hashCode() ^ hints.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        SelectResults other = (SelectResults) o;
        return
               type    == other.type  &&
               count   == other.count &&
               server.equals(other.server) &&
               select.equals(other.select) &&
               search.equals(other.search) &&
               hints.equals(other.hints) &&
               resultSet.equals(other.resultSet) &&
               hasMore == other.hasMore;
    }


}
