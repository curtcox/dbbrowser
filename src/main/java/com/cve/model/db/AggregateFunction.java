package com.cve.model.db;

import com.cve.util.Check;

/**
 * A SQL aggregate function.
 */
public enum AggregateFunction {

        /**
         * Not really an aggregate function at all.
         */
        IDENTITY,
        MIN,
        MAX,
        SUM,
        COUNT
    ;

    /**
     * Given a string, return the matching function.
     */
    public static AggregateFunction parse(String name) {
        Check.notNull(name);
        if (name.trim().isEmpty()) {
            return IDENTITY;
        }
        return valueOf(name.toUpperCase());
    }

}
