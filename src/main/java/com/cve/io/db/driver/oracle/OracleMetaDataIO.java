package com.cve.io.db.driver.oracle;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DefaultDBMetaDataIO;
import com.cve.stores.ManagedFunction;

/**
 *
 * @author curt
 */
final class OracleMetaDataIO extends DefaultDBMetaDataIO {

    protected OracleMetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction) {
        super(connection,managedFunction);
    }

    static DBMetaDataIO of(DBConnection connection, ManagedFunction.Factory managedFunction) {
        return new OracleMetaDataIO(connection,managedFunction);
    }

}
