package com.cve.io.db.driver.mysql;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DefaultDBMetaDataIO;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;

/**
 *
 * @author curt
 */
final class MySQLMetaDataIO extends DefaultDBMetaDataIO {

    protected MySQLMetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction, Log log) {
        super(connection,managedFunction,log);
    }

    static DBMetaDataIO of(DBConnection connection, ManagedFunction.Factory managedFunction, Log log) {
        return new MySQLMetaDataIO(connection,managedFunction,log);
    }

}
