package com.cve.web.db.databases;

import com.cve.web.core.Model;
import com.cve.web.*;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 */
@Immutable
final class DatabasesPage implements Model {

    /**
     * The server that all of the databases come from.
     */
    final DBServer server;

    /**
     * The databases on the server.
     */
    final ImmutableList<Database> databases;

    /**
     * The tables in the databases.
     */
    final ImmutableMultimap<Database,DBTable> tables;

    DatabasesPage(DBServer server, ImmutableList<Database> databases, ImmutableMultimap<Database,DBTable> tables) {
        this.server    = notNull(server);
        this.databases = notNull(databases);
        this.tables    = notNull(tables);
    }
}
