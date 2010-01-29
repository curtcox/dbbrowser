package com.cve.io.db.driver;

import com.cve.io.db.driver.h2.H2Driver;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.JDBCURL;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * Database drivers that we support.
 * @author curt
 */
public final class DBDrivers {

    final Log log = Logs.of();

    final ImmutableList<DBDriver> drivers;

    private DBDrivers(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        
        drivers = driversFor(managedFunction, serversStore,new H2Driver.Factory());
    }

    private static ImmutableList<DBDriver> driversFor(
        ManagedFunction.Factory managedFunction, DBServersStore serversStore, DBDriver.Factory... factories)
    {
        List<DBDriver> drivers = Lists.newArrayList();
        for (DBDriver.Factory factory : factories) {
            drivers.add(factory.of( managedFunction, serversStore));
        }
        return ImmutableList.copyOf(drivers);
    }

    public static DBDrivers of(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return new DBDrivers(managedFunction, serversStore);
    }

    public DBDriver url(JDBCURL url) {
        for (DBDriver driver : drivers) {
            if (driver.handles(url)) {
                return driver;
            }
        }
        String message = url + " not supported";
        throw new IllegalArgumentException(message);
    }

    public Iterable<DBDriver> values() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
