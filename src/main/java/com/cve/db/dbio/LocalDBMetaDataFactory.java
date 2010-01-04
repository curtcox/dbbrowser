package com.cve.db.dbio;

import com.cve.db.Server;
import com.cve.stores.ServersStore;

/**
 * For "direct" access to databases.
 * @author curt
 */
public final class LocalDBMetaDataFactory implements DBMetaData.Factory {

    final ServersStore serversStore;

    private LocalDBMetaDataFactory(ServersStore serversStore) {
        this.serversStore = serversStore;
    }

    public static LocalDBMetaDataFactory of(ServersStore serversStore) {
        return new LocalDBMetaDataFactory(serversStore);
    }

    @Override
    public DBMetaData of(Server server) {
        DBConnection connection = serversStore.getConnection(server);
        return connection.getMetaData();
    }

}
