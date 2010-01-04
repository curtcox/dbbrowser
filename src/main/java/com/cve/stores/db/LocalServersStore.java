package com.cve.stores.db;

import com.cve.db.ConnectionInfo;
import com.cve.db.Server;
import com.google.common.collect.ImmutableList;

/**
 * The database {@link Server}S we know about.
 */
public final class LocalServersStore implements ServersStore {

    @Override
    public ConnectionInfo get(Server key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void put(Server key, ConnectionInfo value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImmutableList<Server> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
