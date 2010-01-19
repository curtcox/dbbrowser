package com.cve.io.db.driver.mssql;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DefaultDBMetaDataIO;
import com.cve.stores.ManagedFunction;

/**
 *
 * @author curt
 */
final class MsSQLTdsMetaDataIO  extends DefaultDBMetaDataIO {

    protected MsSQLTdsMetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction) {
        super(connection,managedFunction);
    }

    static DBMetaDataIO of(DBConnection connection, ManagedFunction.Factory managedFunction) {
        return new MsSQLTdsMetaDataIO(connection,managedFunction);
    }
}
