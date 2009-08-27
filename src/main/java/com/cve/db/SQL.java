package com.cve.db;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A typesafe wrapper for a SQL string.
 * @author curt
 */
@Immutable
public final class SQL {

    /**
     * The actual SQL string.
     */
    private final String sql;

    private SQL(String sql) {
        this.sql = notNull(sql);
    }

    public static SQL of(String sql) {
        return new SQL(sql);
    }

    @Override
    public   int hashCode() { return sql.hashCode(); }

    @Override
    public boolean equals(Object o) {
        SQL sql = (SQL) o;
        return this.sql.equals(sql.sql);
    }

    @Override
    public String toString() { return sql; }
}
