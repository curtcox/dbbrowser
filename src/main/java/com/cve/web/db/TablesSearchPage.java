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
public final class TablesSearchPage implements Model {

    final Search search;

    final Database database;
    /**
     * The tables on the page
     */
    final ImmutableList<DBTable> tables;

    /**
     */
    final ImmutableMultimap<DBTable,DBColumn> columns;

    TablesSearchPage(Search search, Database database,
        List<DBTable> tables, Multimap<DBTable,DBColumn> columns) {
        this.search    = notNull(search);
        this.database  = database;
        this.tables    = ImmutableList.copyOf(notNull(tables));
        this.columns   = ImmutableMultimap.copyOf(notNull(columns));
    }
}
