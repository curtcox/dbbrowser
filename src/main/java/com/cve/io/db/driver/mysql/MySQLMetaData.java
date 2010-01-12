package com.cve.io.db.driver.mysql;

import com.cve.io.db.driver.DefaultDBMetaData;
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
