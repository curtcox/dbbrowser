package com.cve.web.db.servers;

import com.cve.model.db.Database;
import com.cve.web.*;
import com.cve.model.db.DBServer;

import com.cve.util.AnnotatedStackTrace;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 */
final class ServersPage implements Model {

    /**
     * The servers on the page
     */
    final ImmutableList<DBServer> servers;

    /**
     * Server -> { Database , AnnotatedStackTrace }
     * This maps to both database and throwable, because we might not be able
     * to connect to any given database.  Permissions is often, but not always,
     * the reason.
     * Keeping the throwable allows us to diagnose and troubleshoot the
     * reason more easily.
     */
    final ImmutableMultimap<DBServer,Database> databases;

    private ServersPage(ImmutableList<DBServer> servers, ImmutableMultimap<DBServer,Database> databases) {
        this.servers   = notNull(servers);
        this.databases = notNull(databases);
        for (Object value : databases.values()) {
            if (value instanceof Database || value instanceof AnnotatedStackTrace) {
                // OK
            } else {
                String message = "" + value;
                throw new IllegalArgumentException(message);
            }
        }
    }

    static ServersPage of(ImmutableList<DBServer> servers, ImmutableMultimap<DBServer,Database> databases) {
        return new ServersPage(servers,databases);
    }

    @Override
    public String toString() {
        return "<ServersPage>" +
                  " servers=" + servers +
               "</ServersPage>";
    }
}
