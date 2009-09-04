package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.web.*;
import com.cve.db.Server;

import com.cve.util.AnnotatedStackTrace;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 */
public final class ServersSearchPage implements Model {

    final Search search;

    /**
     * The servers on the page
     */
    final ImmutableList<Server> servers;

    /**
     * Server -> { DBColumn , AnnotatedStackTrace }
     * This maps to both column and throwable, because we might not be able
     * to connect to any given database.  Permissions is often, but not always,
     * the reason.
     * Keeping the throwable allows us to diagnose and troubleshoot the
     * reason more easily.
     */
    final ImmutableMultimap<Server,Object> columns;

    ServersSearchPage(Search search, ImmutableList<Server> servers, ImmutableMultimap<Server,Object> columns) {
        this.search    = notNull(search);
        this.servers   = notNull(servers);
        this.columns   = notNull(columns);
        for (Object value : columns.values()) {
            if (value instanceof DBColumn || value instanceof AnnotatedStackTrace) {
                // OK
            } else {
                String message = "" + value;
                throw new IllegalArgumentException(message);
            }
        }
    }
}
