package com.cve.io.db.driver;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.DBServersStore;

/**
 *
 * @author curt
 */
public final class CompositeDBDriverFactory implements DBDriver.Factory {

    final Log log = Logs.of();

    private CompositeDBDriverFactory() {
        
    }

    @Override
    public DBDriver of( Factory managedFunction, DBServersStore serversStore) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
