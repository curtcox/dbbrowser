package com.cve.web.db;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.log.Log;
import com.cve.sample.db.SampleH2Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBHintsStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.cve.web.*;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class TablesHandlerTest {

    Log log;
    final DBHintsStore hintsStore = MemoryDBHintsStore.of();
    final DBServersStore serversStore = MemoryDBServersStore.of();
    final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    final DBMetaData.Factory db = LocalDBMetaDataFactory.of(serversStore,managedFunction,log);
    final TablesHandler handler = TablesHandler.of(db,serversStore,hintsStore,managedFunction,log);
    {
        SampleH2Server.addToStore(serversStore);
    }

    @Test
    public void handlesTablesOnlyRequest() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("//SAMPLE/db/");
        assertNotNull(handler.produce(request));
    }

    @Test
    public void isTablesOnlyRequest() {
        assertFalse(TablesHandler.isTablesOnlyRequest("//server/"));
        assertFalse(TablesHandler.isTablesOnlyRequest("//server:8080/"));
        assertTrue(TablesHandler.isTablesOnlyRequest("//server/db/"));
        assertFalse(TablesHandler.isTablesOnlyRequest("//server/db/table/"));
    }

    @Test
    public void get() {
        PageRequest request = null;
        handler.get(request);
    }
}
