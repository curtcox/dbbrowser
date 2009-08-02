package com.cve.db;

import com.cve.web.Model;
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

    public enum Type {
        NORMAL_DATA,
        COLUMN_VALUE_DISTRIBUTION
    }

    /**
     * The select used to generate the results
     */
    private final Select select;

    /**
     * The results of the select
     */
    private final DBResultSet resultSet;

    /**
     * Hints about how the results should be displayed.
     */
    private final Hints hints;

    /**
     * The total number of rows that would be in the result set if no limit
     * were used.
     */
    private final int count;

    /**
     * True if there are more rows available for the select, after the ones
     * in the result set.
     */
    private final boolean hasMore;

    /**
     * The type of results these are.
     */
    private final Type type;


    private SelectResults(Type type, Select select, DBResultSet resultSet, Hints hints, int count, boolean hasMore) {
        this.type      = notNull(type);
        this.select    = notNull(select);
        this.resultSet = notNull(resultSet);
        this.hints     = notNull(hints);
        this.count     = count;
        this.hasMore   = hasMore;
    }

    public static SelectResults selectResultsHintsMore(
        Select select, DBResultSet resultSet, Hints hints, boolean hasMore)
    {
        return new SelectResults(Type.NORMAL_DATA,select,resultSet,hints,100,hasMore);
    }

    public static SelectResults typeSelectResultsHintsCountMore(
        Type type, Select select, DBResultSet resultSet, Hints hints, int count, boolean hasMore)
    {
        return new SelectResults(type,select,resultSet,hints,count,hasMore);
    }

    public Select       getSelect() { return select;    }
    public DBResultSet getResultSet() { return resultSet; }
    public Hints         getHints() { return hints;     }
    public Type           getType() { return type;      }
    public int           getCount() { return count;     }
    public boolean        hasMore() { return hasMore;   }

    @Override
    public int hashCode() {
        return select.hashCode() ^ resultSet.hashCode() ^ hints.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        SelectResults other = (SelectResults) o;
        return select.equals(other.select) &&
                hints.equals(other.hints) &&
                resultSet.equals(other.resultSet) &&
                hasMore == other.hasMore;
    }


}
