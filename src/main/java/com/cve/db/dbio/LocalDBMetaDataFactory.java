package com.cve.db.dbio;

import com.cve.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;

/**
 * For "direct" access to databases.
 * @author curt
 */
public final class LocalDBMetaDataFactory implements DBMetaData.Factory {

    final DBServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    private LocalDBMetaDataFactory(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = serversStore;
        this.managedFunction = managedFunction;
    }

    public static LocalDBMetaDataFactory of(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new LocalDBMetaDataFactory(serversStore,managedFunction);
    }

    @Override
    public DBMetaData of(DBServer server) {
        DBConnection connection = DBConnectionFactory.getConnection(server, serversStore, managedFunction);
        return connection.getMetaData();
    }

}
