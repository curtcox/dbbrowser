package com.cve.sample.db;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.DefaultDBConnection;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.io.db.driver.h2.H2Driver;
import com.cve.log.Log;
import com.cve.model.db.DBConnectionInfo;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;

/**
 *
 * @author curt
 */
public final class DBSampleServerTestObjects {

    public static final Log log = null;
    public static final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    public static final DBServersStore serversStore = MemoryDBServersStore.of();
    public static final DBMetaData.Factory db = LocalDBMetaDataFactory.of(serversStore,managedFunction,log);
    public static final DBConnectionInfo info = SampleH2Server.getConnectionInfo();
    public static final DefaultDBConnection connection = DefaultDBConnection.of(info,serversStore,managedFunction,log);
    public static final DBMetaData meta = H2Driver.of(log,managedFunction,serversStore).getDBMetaData(connection);

    {
        try {
            SampleH2Server.of();
            SampleH2Server.addToStore(serversStore);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
