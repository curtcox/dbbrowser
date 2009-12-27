package com.cve.stores;

import com.cve.db.ConnectionInfo;
import com.cve.db.dbio.DBConnection;
import com.cve.db.Database;
import com.cve.db.Server;
import com.google.common.collect.ImmutableList;

/**
 * The datbase {@link Server}S we know about.
 */
public interface ServersStore {

    /**
     * Get a list of all servers in the store.
     */
    ImmutableList<Server> getServers();

    /**
     * Get a connection to the given server.
     */
    DBConnection getConnection(Server server);

    /**
     * Get a connection to the given server and database.
     */
    DBConnection getConnection(Server server, Database database);

    /**
     * Add a server and how to connect to it.
     */
    void addServer(Server server, ConnectionInfo info);

    /**
     * Load the list of servers we know about from disk.
     * While it would be nice to be able to do this automatically in a static
     * initializer, doing so makes it harder to write a complete set of
     * unit tests.
     */
    void load();

}
