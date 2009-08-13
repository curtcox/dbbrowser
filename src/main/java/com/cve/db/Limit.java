package com.cve.db;

import javax.annotation.concurrent.Immutable;

import static com.cve.util.Check.notNegative;
/**
 * A SQL select statement limit clause.
 * @author curt
 */
@Immutable
public final class Limit {

    public final int limit;
    public final int offset;

    public static final Limit DEFAULT = limit(20);

    private Limit(int limit, int offset) {
        this.limit  = notNegative(limit);
        this.offset = notNegative(offset);
    }

    public static Limit limitOffset(int limit, int offset) {
        return new Limit(limit,offset);
    }

    public static Limit limit(int limit) {
        return new Limit(limit,0);
    }

    /**
     * Get a new limit by paging this one forward.
     */
    public Limit next(int pages) {
        return limitOffset(limit,offset + (pages * limit));
    }

    /**
     * Get a new limit by paging this one backward.
     */
    public Limit back(int pages) {
        return limitOffset(limit,offset - (pages * limit));
    }

    /**
     * Get a new bigger limit.
     */
    public Limit bigger(int factor) {
        return limitOffset(limit * factor,offset);
    }

    /**
     * Get a new samller limit.
     */
    public Limit smaller(int factor) {
        return limitOffset(limit / factor,offset);
    }

    @Override
    public int hashCode() { return limit ^ offset; }

    public boolean equals(Object o) {
        Limit other = (Limit) o;
        return limit  == other.limit &&
               offset == other.offset;
    }

    @Override
    public String toString() {
        return "limit=" + limit + " offset=" + offset;
    }
}
