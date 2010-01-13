package com.cve.io.db.driver.mysql;

import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.driver.DefaultDBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;

/**
 *
 * @author curt
 */
final class MySQLMetaData extends DefaultDBMetaData {

    private MySQLMetaData(DBMetaDataIO dbmd, ManagedFunction.Factory managedFunction,DBServersStore serversStore) {
        super(dbmd,managedFunction,serversStore);
    }

    static MySQLMetaData of(DBMetaDataIO dbmd, ManagedFunction.Factory managedFunction,DBServersStore serversStore) {
        return new MySQLMetaData(dbmd,managedFunction,serversStore);
    }
}
