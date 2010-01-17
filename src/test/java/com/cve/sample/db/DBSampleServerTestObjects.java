package com.cve.sample.db;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.DefaultDBConnection;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.io.db.driver.DefaultDBMetaData;
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

    public static final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    public static final DBServersStore serversStore = MemoryDBServersStore.of();
    public static final DBMetaData.Factory db = LocalDBMetaDataFactory.of(serversStore,managedFunction);
    public static final DBConnectionInfo info = SampleH2Server.getConnectionInfo();
    public static final DefaultDBConnection connection = DefaultDBConnection.of(info,serversStore,managedFunction);
    public static final DBMetaData meta = DefaultDBMetaData.getDbmd(connection,managedFunction,serversStore);

    static {
        try {
            SampleH2Server.of();
            SampleH2Server.addToStore(serversStore);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
