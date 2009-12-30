package com.cve.db.dbio;

import com.cve.db.Server;
import com.cve.stores.ServersStore;
import com.cve.stores.Stores;

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
        DBConnection connection = Stores.getServerStore().getConnection(server);
        return connection.getMetaData();
    }

}
