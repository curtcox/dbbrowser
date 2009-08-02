package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 */
public final class TablesPage implements Model {

    final Server server;
    final Database database;
    final ImmutableList<DBTable> tables;
    final ImmutableMultimap<DBTable,DBColumn> columns;

    TablesPage(Server server, Database database, ImmutableList<DBTable> tables, ImmutableMultimap<DBTable,DBColumn> columns) {
        this.server    = notNull(server);
        this.database  = notNull(database);
        this.tables    = notNull(tables);
        this.columns   = notNull(columns);
    }
}
