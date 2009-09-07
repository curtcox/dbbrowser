package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.web.*;

import com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 */
public final class DatabasesSearchPage implements Model {

    final Search search;

    /**
     * The servers on the page
     */
    final ImmutableList<Database> databases;

    /**
     */
    final ImmutableMultimap<DBTable,DBColumn> columns;

    final ImmutableMultimap<Database,DBTable> tables;

    DatabasesSearchPage(Search search,
        List<Database> databases,
        Multimap<Database,DBTable> tables,
        Multimap<DBTable,DBColumn> columns) {
        this.search    = notNull(search);
        this.databases = ImmutableList.copyOf(notNull(databases));
        this.tables    = ImmutableMultimap.copyOf(notNull(tables));
        this.columns   = ImmutableMultimap.copyOf(notNull(columns));
    }
}
