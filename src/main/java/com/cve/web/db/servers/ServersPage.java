package com.cve.web.db.servers;

import com.cve.db.Database;
import com.cve.web.*;
import com.cve.db.Server;

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
    final ImmutableList<Server> servers;

    /**
     * Server -> { Database , AnnotatedStackTrace }
     * This maps to both database and throwable, because we might not be able
     * to connect to any given database.  Permissions is often, but not always,
     * the reason.
     * Keeping the throwable allows us to diagnose and troubleshoot the
     * reason more easily.
     */
    final ImmutableMultimap<Server,Object> databases;

    ServersPage(ImmutableList<Server> servers, ImmutableMultimap<Server,Object> databases) {
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
}
