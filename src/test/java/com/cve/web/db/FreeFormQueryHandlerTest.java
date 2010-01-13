package com.cve.web.db;

import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class FreeFormQueryHandlerTest {

    final DBServersStore serversStore = MemoryDBServersStore.of();
    final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();

    final FreeFormQueryHandler handler = FreeFormQueryHandler.of(serversStore,managedFunction);

    @Test
    public void doesntHandleServerWithNoQuery() {
        assertFalse(handler.handles("/server/"));
        assertFalse(handler.handles("/server"));
    }

    @Test
    public void handlesQueryWithJustServer() {
        assertTrue(handler.handles("/server/select?q=*+from+bar"));
    }

    @Test
    public void handlesQueryWithServerAndDatabase() {
        assertTrue(handler.handles("/server/database/select?q=*+from+bar"));
    }

    @Test
    public void doesntHandleServerAndDatabaseWithNoQuery() {
        assertFalse(handler.handles("/server/database/"));
        assertFalse(handler.handles("/server/database"));
    }

}
