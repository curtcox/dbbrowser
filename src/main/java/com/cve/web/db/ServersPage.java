package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 */
public final class ServersPage implements Model {

    final ImmutableList<Server> servers;

    /**
     * Server -> { Database , Throwable }
     */
    final ImmutableMultimap<Server,Object> databases;

    ServersPage(ImmutableList<Server> servers, ImmutableMultimap<Server,Object> databases) {
        this.servers   = notNull(servers);
        this.databases = notNull(databases);
    }
}
