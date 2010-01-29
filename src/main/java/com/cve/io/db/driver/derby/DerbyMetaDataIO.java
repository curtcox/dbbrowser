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

    protected DerbyMetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction) {
        super(connection,managedFunction);
    }

    static DBMetaDataIO of(DBConnection connection, ManagedFunction.Factory managedFunction) {
        return new DerbyMetaDataIO(connection,managedFunction);
    }
}
