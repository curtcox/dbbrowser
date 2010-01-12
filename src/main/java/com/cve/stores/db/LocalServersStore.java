package com.cve.stores.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBServer;
import com.google.common.collect.ImmutableList;

/**
 * The database {@link Server}S we know about.
 */
public final class LocalServersStore implements DBServersStore {

    @Override
    public DBConnectionInfo get(DBServer key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void put(DBServer key, DBConnectionInfo value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImmutableList<DBServer> keys() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
