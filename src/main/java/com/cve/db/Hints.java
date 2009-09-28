package com.cve.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
import com.cve.util.Canonicalizer;

/**
 * Hints are known things about a {@link Database} that are useful for
 * making queries.  Generally, hints map one-to-one with result sets, although
 * hints could be for a database, a table, a server, or a a single column.
 * Currently, these include {@link Join}S and {@link Filter}S.
 */
@Immutable
public final class Hints {

    /**
     * Joins to other tables.
     */
    public final ImmutableSet<Join>   joins;

    /**
     * Filters that could be applied.
     */
    public final ImmutableSet<Filter> filters;

    /**
     * Columns in the result set tables, but not necessarily in the result set.
     * These represent columns that could be added without a join.
     */
    public final ImmutableSet<DBColumn> columns;

    private static final Canonicalizer<Hints> CANONICALIZER = Canonicalizer.of();

    private static Hints canonical(Hints hints) {
        return CANONICALIZER.canonical(hints);
    }

    /**
     * No hints
     */
    public static final Hints NONE = new Hints(set(),set(),set());

    private Hints(
        Collection<Join> joins, Collection<Filter> filters,
        Collection<DBColumn> columns)
    {
        this.joins       = ImmutableSet.copyOf(notNull(joins));
        this.filters     = ImmutableSet.copyOf(notNull(filters));
        this.columns     = ImmutableSet.copyOf(notNull(columns));
    }

    public static Hints of(
        Collection<Join> joins, Collection<Filter> filters, Collection<DBColumn> columns)
    {
        return canonical(new Hints(joins,filters,columns));
    }

    public static Hints of(Join join) {
        return canonical(new Hints(set(join),set(),set()));
    }

    public static Hints of(Join join, DBColumn column) {
        return canonical(new Hints(set(join),set(),set(column)));
    }

    private static ImmutableSet                      set() { return ImmutableSet.of(); }
    private static <T> ImmutableSet<T>     set(T... items) { return ImmutableSet.of(items); }

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
    public int hashCode() { return joins.hashCode() ^ filters.hashCode() ^ columns.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        Hints other = (Hints) o;
        return joins.equals(other.joins) && filters.equals(other.filters) && columns.equals(other.columns);
    }

    @Override
    public String toString() {
        return "joins=" + joins + " filters=" + filters;
    }
}
