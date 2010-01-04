package com.cve.db.dbio;

import com.cve.db.Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.ServersStore;

/**
 * For "direct" access to databases.
 * @author curt
 */
public final class LocalDBMetaDataFactory implements DBMetaData.Factory {

    final ServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    private LocalDBMetaDataFactory(ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = serversStore;
        this.managedFunction = managedFunction;
    }

    public static LocalDBMetaDataFactory of(ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new LocalDBMetaDataFactory(serversStore,managedFunction);
    }

    @Override
    public DBMetaData of(Server server) {
        DBConnection connection = DBConnectionFactory.getConnection(server, serversStore, managedFunction);
        return connection.getMetaData();
    }

}
