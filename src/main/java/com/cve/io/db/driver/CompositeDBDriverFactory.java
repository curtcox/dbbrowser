package com.cve.io.db.driver;

import com.cve.log.Log;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.DBServersStore;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
public final class CompositeDBDriverFactory implements DriverIO.Factory {

    final Log log;

    private CompositeDBDriverFactory(Log log) {
        this.log = notNull(log);
    }

    @Override
    public DriverIO of(Log log, Factory managedFunction, DBServersStore serversStore) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
