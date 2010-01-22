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

    Log log;
    public final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    public final DBServersStore serversStore = MemoryDBServersStore.of();
    public final DBMetaData.Factory db = LocalDBMetaDataFactory.of(serversStore,managedFunction,log);
    public final DBConnectionInfo info = SampleH2Server.getConnectionInfo();
    public final DefaultDBConnection connection = DefaultDBConnection.of(info,serversStore,managedFunction,log);
    public final DBMetaData meta = H2Driver.of().getDBMetaData(connection,managedFunction,serversStore,log);

    {
        try {
            SampleH2Server.of();
            SampleH2Server.addToStore(serversStore);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
