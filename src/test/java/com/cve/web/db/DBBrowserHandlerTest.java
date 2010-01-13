package com.cve.web.db;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBHintsStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.cve.util.URIs;
import com.cve.web.*;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class DBBrowserHandlerTest {

    final DBServersStore serversStore = MemoryDBServersStore.of();
    final DBHintsStore hintsStore = MemoryDBHintsStore.of();
    final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    final DBMetaData.Factory db = LocalDBMetaDataFactory.of(serversStore,managedFunction);
    final RequestHandler handler = DBBrowserHandler.of(db,serversStore,hintsStore,managedFunction);
    {
        DBServer server = DBServer.uri(URIs.of("server"));
        serversStore.put(server, DBConnectionInfo.NULL);
    }

    @Test
    public void handle() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("//server/");
        assertNotNull(handler.produce(request));
    }
}
