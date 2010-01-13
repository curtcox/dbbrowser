package com.cve.db.sample;

import com.cve.sample.db.SampleH2Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class SampleServerTest {

    @Test
    public void of() {
        DBServersStore serversStore = MemoryDBServersStore.of();
        ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
        SampleH2Server.of(serversStore, managedFunction);
    }
}
