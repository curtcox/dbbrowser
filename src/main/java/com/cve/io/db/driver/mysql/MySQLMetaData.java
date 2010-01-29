package com.cve.io.db.driver.mysql;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.driver.DefaultDBMetaData;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;

/**
 *
 * @author curt
 */
final class MySQLMetaData extends DefaultDBMetaData {

    private MySQLMetaData(DBMetaDataIO io, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        super(io,managedFunction,serversStore);
    }

    static DBMetaData of(DBConnection connection, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        DBMetaDataIO io = MySQLMetaDataIO.of(connection,managedFunction);
        return new MySQLMetaData(io,managedFunction,serversStore);
    }

}
