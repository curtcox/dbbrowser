package com.cve.stores;

import com.cve.db.DBColumn;
import com.cve.db.Filter;
import com.cve.db.Hints;
import com.cve.db.Join;
import com.cve.db.DBTable;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBMetaData;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

/**
 * The {@link Hints} we know about.
 * There are several sources for hints.
 * <ol>
 * <li> Foreign key relationships defined on the database.
 * <li> Hints we have put in the store "manually" -- like with a hint file.
 * <li> Hints we have mined from:
 *    <ol>
 *        <li> the existing database data
 *        <li> SQL in log files or source code
 *    </ol>
 * </ol>
 */
public final class HintsStore {

    private static Multimap<DBColumn,Join>     joins = HashMultimap.create();
    private static Multimap<DBColumn,Filter> filters = HashMultimap.create();

    public static void putHints(Hints hints) {
        for (Join join : hints.getJoins()) {
            joins.put(join.getSource(), join);
        }
        for (Filter filter : hints.getFilters()) {
            filters.put(filter.getColumn(), filter);
        }
    }

    public static Hints getHints(ImmutableList<DBColumn> columns) throws SQLException {
        Set<DBColumn>    keySet = Sets.newHashSet();
        Set<Join>      joinSet = Sets.newHashSet();
        Set<Filter>  filterSet = Sets.newHashSet();
        for (DBColumn column : columns) {
            joinSet.addAll(joins.get(column));
            filterSet.addAll(filters.get(column));
        }
        ImmutableList<DBTable> tables = spanningTables(columns);
        Server                 server = tables.get(0).getDatabase().getServer();
        DBMetaData               meta = DBConnection.getDbmd(server);
        joinSet.addAll(meta.getJoinsFor(tables));
        keySet.addAll(meta.getPrimaryKeysFor(tables));
        return Hints.of(
            ImmutableList.copyOf(keySet),
            ImmutableList.copyOf(joinSet),
            ImmutableList.copyOf(filterSet),
            meta.getColumnsFor(tables)
        );
    }

    static ImmutableList<DBTable> spanningTables(Collection<DBColumn> columns) {
        Set<DBTable>  tables = Sets.newHashSet();
        for (DBColumn column : columns) {
            tables.add(column.getTable());
        }
        return ImmutableList.copyOf(tables);
    }
}
