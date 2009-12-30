package com.cve.stores;

import com.cve.db.dbio.DBMetaData.Factory;

/**
 *
 */
public final class Stores {

    private final static ServersStore SERVERS = new LocalServersStore();

    public static ServersStore getServerStore() {
        return SERVERS;
    }

    public static HintsStore getHintsStore(Factory db) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
