package com.cve.io.db;

import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import static com.cve.util.Check.notNull;

/**
 * For "direct" access to databases.
 * @author curt
 */
public final class LocalDBMetaDataFactory implements DBMetaData.Factory {

    final DBServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    private LocalDBMetaDataFactory(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
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
