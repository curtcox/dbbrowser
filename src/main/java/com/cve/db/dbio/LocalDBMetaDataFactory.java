package com.cve.db.dbio;

import com.cve.db.Server;
import com.cve.stores.ServersStore;

/**
 * For "direct" access to databases.
 * @author curt
 */
public final class LocalDBMetaDataFactory implements DBMetaData.Factory {

    private LocalDBMetaDataFactory() {}

    public static LocalDBMetaDataFactory of() {
        return new LocalDBMetaDataFactory();
    }

    @Override
    public DBMetaData of(Server server) {
        DBConnection connection = ServersStore.getConnection(server);
        return connection.getMetaData();
    }

}
