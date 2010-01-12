package com.cve.model.db;

import javax.annotation.concurrent.Immutable;

import static com.cve.util.Check.notNegative;
/**
 * A SQL select statement limit clause.
 * @author curt
 */
@Immutable
public final class DBLimit {

    /**
     * Maximum number of rows to return.
     */
    public final int limit;

    /**
     * Offset within the entire result set of the first row to return.
     */
    public final int offset;

    public static final DBLimit DEFAULT = limit(20);

    private DBLimit(int limit, int offset) {
        this.limit  = notNegative(limit);
        this.offset = notNegative(offset);
    }

    public static DBLimit limitOffset(int limit, int offset) {
        return new DBLimit(limit,offset);
    }

    public static DBLimit limit(int limit) {
        return new DBLimit(limit,0);
    }

    /**
     * Get a new limit by paging this one forward.
     */
    public DBLimit next(int pages) {
        return limitOffset(limit,offset + (pages * limit));
    }

    /**
     * Get a new limit by paging this one backward.
     */
    public DBLimit back(int pages) {
        return limitOffset(limit,offset - (pages * limit));
    }

    /**
     * Get a new bigger limit.
     */
    public DBLimit bigger(int factor) {
        return limitOffset(limit * factor,offset);
    }

    /**
     * Get a new samller limit.
     */
    public DBLimit smaller(int factor) {
        return limitOffset(limit / factor,offset);
    }

    @Override
    public int hashCode() { return limit ^ offset; }

    public boolean equals(Object o) {
        DBLimit other = (DBLimit) o;
        return limit  == other.limit &&
               offset == other.offset;
    }

    @Override
    public String toString() {
        return "limit=" + limit + " offset=" + offset;
    }
}
