package com.cve.io.db.driver.mysql;

import com.cve.io.db.driver.h2.*;
import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DefaultDBMetaDataIO;
import com.cve.stores.ManagedFunction;

/**
 *
 * @author curt
 */
final class MySQLMetaDataIO extends DefaultDBMetaDataIO {

    protected MySQLMetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction) {
        super(connection,managedFunction);
    }

    static DBMetaDataIO of(DBConnection connection, ManagedFunction.Factory managedFunction) {
        return new MySQLMetaDataIO(connection,managedFunction);
    }

}
