package com.cve.db.dbio.driver;

import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;

/**
 *
 * @author curt
 */
final class MySQLMetaData extends DefaultDBMetaData {

    public MySQLMetaData(ManagedFunction.Factory managedFunction,DBServersStore serversStore) {
        super(managedFunction,serversStore);
    }

}
