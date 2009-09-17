package com.cve.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Hints are known things about a {@link Database} that are useful for
 * making queries.  Currently, these include {@link Join}S and {@link Filter}S.
 */
@Immutable
public final class Hints {

    /**
     * Primary keys for tables in the result set.
     */
    public final ImmutableList<DBColumn> primaryKeys;

    /**
     * Joins to other tables.
     */
    public final ImmutableList<Join>   joins;

    /**
     * Filters that could be applied.
     */
    public final ImmutableList<Filter> filters;

    /**
     * Columns in the result set tables, but not necessarily in the result set.
     * These represent columns that could be added without a join.
     */
    public final ImmutableList<DBColumn> columns;

    
    public static final Hints NONE = new Hints(list(),list(),list(),list());

    private Hints(
        ImmutableList<DBColumn> primaryKeys,
        ImmutableList<Join> joins, ImmutableList<Filter> filters,
        ImmutableList<DBColumn> columns)
    {
        this.primaryKeys = notNull(primaryKeys);
        this.joins       = notNull(joins);
        this.filters     = notNull(filters);
        this.columns     = notNull(columns);
    }

    public static Hints of(
        ImmutableList<DBColumn> primaryKeys, ImmutableList<Join> joins, ImmutableList<Filter> filters, ImmutableList<DBColumn> columns)
    {
        return new Hints(primaryKeys,joins,filters,columns);
    }

    public static Hints of(Join join) {
        return new Hints(list(),list(join),list(),list());
    }

    public static Hints of(Join join, DBColumn column) {
        return new Hints(list(),list(join),list(),list(column));
    }

    private static ImmutableList                      list() { return ImmutableList.of(); }
    private static <T> ImmutableList<T>     list(T... items) { return ImmutableList.of(items); }

    /**
     * Return the joins from these hints relevant to the given column.
     */
    public ImmutableList<Join> getJoinsFor(DBColumn column) {
        notNull(column);
        List<Join> list = Lists.newArrayList();
        for (Join join : joins) {
            if (column.equals(join.dest) || column.equals(join.source)) {
                list.add(join);
            }
        }
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<Filter> getFiltersFor(DBColumn column) {
        notNull(column);
        List<Filter> list = Lists.newArrayList();
        for (Filter filter : filters) {
            if (column.equals(filter.column)) {
                list.add(filter);
            }
        }
        return ImmutableList.copyOf(list);
    }

    @Override
    public int hashCode() { return joins.hashCode() ^ filters.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        Hints other = (Hints) o;
        return joins.equals(other.joins) && filters.equals(other.filters);
    }

    @Override
    public String toString() {
        return "joins=" + joins + " filters=" + filters;
    }
}
