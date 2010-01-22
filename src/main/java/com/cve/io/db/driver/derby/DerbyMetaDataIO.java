package com.cve.io.db.driver.derby;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DefaultDBMetaDataIO;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;

/**
 *
 * @author curt
 */
final class DerbyMetaDataIO extends DefaultDBMetaDataIO {

    protected DerbyMetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction, Log log) {
        super(connection,managedFunction,log);
    }

    static DBMetaDataIO of(DBConnection connection, ManagedFunction.Factory managedFunction, Log log) {
        return new DerbyMetaDataIO(connection,managedFunction,log);
    }
}
