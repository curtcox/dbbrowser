package com.cve.io.db.driver.mssql;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DefaultDBMetaDataIO;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;

/**
 *
 * @author curt
 */
final class MsSQLTdsMetaDataIO  extends DefaultDBMetaDataIO {

    protected MsSQLTdsMetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction, Log log) {
        super(connection,managedFunction,log);
    }

    static DBMetaDataIO of(DBConnection connection, ManagedFunction.Factory managedFunction, Log log) {
        return new MsSQLTdsMetaDataIO(connection,managedFunction,log);
    }
}
