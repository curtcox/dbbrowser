package com.cve.db.dbio.driver;

import com.cve.stores.ManagedFunction;
import com.cve.stores.ServersStore;

/**
 *
 * @author curt
 */
final class MySQLMetaData extends DefaultDBMetaData {

    public MySQLMetaData(ManagedFunction.Factory managedFunction,ServersStore serversStore) {
        super(managedFunction,serversStore);
    }

}
