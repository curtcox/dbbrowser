package com.cve.web.db.servers;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.sample.db.SampleH2Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
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
public class ServersHandlerTest {

    final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    final DBServersStore serversStore = MemoryDBServersStore.of();
    final DBMetaData.Factory db = LocalDBMetaDataFactory.of(serversStore,managedFunction);
    final ServersHandler handler = ServersHandler.of(db,serversStore);
    {
        SampleH2Server.of(serversStore, managedFunction);
        SampleH2Server.addToStore(serversStore);
    }

    @Test
    public void producesServersOnlyRequest() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/");
        PageResponse response = handler.produce(request);
        assertNotNull(response);
    }

    @Test
    public void getsServersOnlyRequest() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/");
        ServersPage model = (ServersPage) handler.get(request);
        assertNotNull(model);
        assertEquals(1,model.servers.size());
        assertEquals(1,model.databases.size());
    }

    @Test
    public void handlesServersOnlyRequestWithSearch() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/search/");
        assertNotNull(handler.produce(request));
    }

    @Test
    public void skipsNonServersOnlyRequest() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/+/server/");
        assertNull(handler.produce(request));
    }

}
