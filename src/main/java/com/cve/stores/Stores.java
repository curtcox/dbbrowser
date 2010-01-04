package com.cve.stores;

import com.cve.db.dbio.DBMetaData.Factory;

/**
 *
 */
public final class Stores {

    public static ServersStore getServerStore(ManagedFunction.Factory managedFunction) {
        return LocalServersStore.of(managedFunction);
    }

    public static HintsStore getHintsStore(Factory db) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
