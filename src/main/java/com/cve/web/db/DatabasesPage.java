package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 */
@Immutable
public final class DatabasesPage implements Model {

    final Server server;
    final ImmutableList<Database> databases;
    final ImmutableMultimap<Database,DBTable> tables;

    DatabasesPage(Server server, ImmutableList<Database> databases, ImmutableMultimap<Database,DBTable> tables) {
        this.server    = notNull(server);
        this.databases = notNull(databases);
        this.tables    = notNull(tables);
    }
}
