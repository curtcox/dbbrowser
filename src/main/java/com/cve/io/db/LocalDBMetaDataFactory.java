package com.cve.io.db;

import com.cve.log.Log;
import com.cve.log.Logs;
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

    final Log log = Logs.of();

    private LocalDBMetaDataFactory(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
        
        connections = DBConnectionFactory.of(serversStore, managedFunction);
    }

    public static LocalDBMetaDataFactory of(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new LocalDBMetaDataFactory(serversStore,managedFunction);
    }

    @Override
    public DBMetaData of(DBServer server) {
        DBConnection connection = connections.getConnection(server);
        return connection.getMetaData();
    }

}
