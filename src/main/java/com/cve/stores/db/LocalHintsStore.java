package com.cve.stores.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Hints;
import com.cve.model.db.Join;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import static com.cve.util.Check.notNull;


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
final class LocalHintsStore implements DBHintsStore {

    final Log log = Logs.of();
    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    private LocalHintsStore(DBMetaData.Factory db) {
        this.db = notNull(db);
        
    }

    public static LocalHintsStore of(DBMetaData.Factory db) {
        return new LocalHintsStore(db);
    }

    /**
     * Columns -> joins they participate in.
     */
    private static Multimap<DBColumn,Join>     joins = HashMultimap.create();

    /**
     * Columns -> useful filters for that column.
     */
    private static Multimap<DBColumn,DBRowFilter> filters = HashMultimap.create();

    /**
     * Add these hints to those in the store.
     */
    public static void putHints(Hints hints) {
        for (Join join : hints.joins) {
            joins.put(join.source, join);
        }
        for (DBRowFilter filter : hints.filters) {
            filters.put(filter.column, filter);
        }
    }

    @Override
    public Hints get(ImmutableList<DBColumn> columns) {
        log.args(columns);
        Set<Join>      joinSet = Sets.newHashSet();
        Set<DBRowFilter>  filterSet = Sets.newHashSet();
        for (DBColumn column : columns) {
            joinSet.addAll(joins.get(column));
            filterSet.addAll(filters.get(column));
        }
        ImmutableList<DBTable> tables = spanningTables(columns);
        DBServer                 server = tables.get(0).database.server;
        DBMetaData               meta = db.of(server);
        joinSet.addAll(meta.getJoinsFor(tables).value);
        return Hints.of(
            joinSet,
            filterSet,
            meta.getColumnsFor(tables).value
        );
    }

    ImmutableList<DBTable> spanningTables(Collection<DBColumn> columns) {
        log.args(columns);
        Set<DBTable>  tables = Sets.newHashSet();
        for (DBColumn column : columns) {
            tables.add(column.table);
        }
        return ImmutableList.copyOf(tables);
    }

    @Override
    public void put(ImmutableList<DBColumn> key, Hints value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImmutableList<ImmutableList<DBColumn>> keys() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
