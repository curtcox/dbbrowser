package com.cve.web.db.servers;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.web.*;
import com.cve.db.Server;

import com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 */
final class ServersSearchPage implements Model {

    final Search search;

    /**
     * The servers on the page
     */
    final ImmutableList<Server> servers;

    final ImmutableMultimap<Server,Database> databases;

    /**
     */
    final ImmutableMultimap<DBTable,DBColumn> columns;

    final ImmutableMultimap<Database,DBTable> tables;

    ServersSearchPage(Search search,
        List<Server> servers,
        Multimap<Server,Database> databases,
        Multimap<Database,DBTable> tables,
        Multimap<DBTable,DBColumn> columns) {
        this.search    = notNull(search);
        this.servers   = ImmutableList.copyOf(notNull(servers));
        this.databases = ImmutableMultimap.copyOf(notNull(databases));
        this.tables    = ImmutableMultimap.copyOf(notNull(tables));
        this.columns   = ImmutableMultimap.copyOf(notNull(columns));
    }
}
