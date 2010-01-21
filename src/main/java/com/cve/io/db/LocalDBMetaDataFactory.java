package com.cve.io.db;

import com.cve.log.Log;
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

    final DBConnectionFactory connections;

    final Log log;

    private LocalDBMetaDataFactory(DBServersStore serversStore, ManagedFunction.Factory managedFunction, Log log) {
        this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
        this.log = notNull(log);
        connections = DBConnectionFactory.of(serversStore, managedFunction, log);
    }

    public static LocalDBMetaDataFactory of(DBServersStore serversStore, ManagedFunction.Factory managedFunction, Log log) {
        return new LocalDBMetaDataFactory(serversStore,managedFunction,log);
    }

    @Override
    public DBMetaData of(DBServer server) {
        DBConnection connection = connections.getConnection(server);
        return connection.getMetaData();
    }

}
