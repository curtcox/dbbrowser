
package com.cve.web.db;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.log.Log;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.cve.util.URIs;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.cve.html.HTML.*;
/**
 *
 * @author curt
 */
public class DatabaseMetaHandlerTest {

    Log log;

    private DBServer getStoreServer() {
        DBServer server = DBServer.uri(URIs.of("server"),log);
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:db"));
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL, "", "");
        return server;
    }

    @Test
    public void getCatalogs() throws SQLException {
        DBServer server = getStoreServer();
        String unnamed = borderTable(tr(th("CATALOG_NAME")) + tr(td("UNNAMED")) );
        String db1 = borderTable(tr(th("CATALOG_NAME")) + tr(td("DB1")) );
        String actual = newHandler().getCatalogs(server);
        assertTrue(actual.equals(unnamed) || actual.equals(db1));
    }

    @Test
    public void isDatabaseMetaRequest() {
        DatabaseMetaHandler handler = newHandler();
        assertFalse(handler.isDatabaseMetaRequest("/server/"));
        assertFalse(handler.isDatabaseMetaRequest("/server:8080/"));
        assertFalse(handler.isDatabaseMetaRequest("/server/db/"));
        assertFalse(handler.isDatabaseMetaRequest("/server/db/foo/"));
        assertFalse(handler.isDatabaseMetaRequest("/meaty/server/"));
        assertFalse(handler.isDatabaseMetaRequest("/motto/server/"));
        assertTrue(handler.isDatabaseMetaRequest("/meta/server/"));
        assertTrue(handler.isDatabaseMetaRequest("/meta/server/catalogs/"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void badRequestThrowsException() throws SQLException {
        DatabaseMetaHandler handler = newHandler();
        handler.tryPage(DBServer.uri(URIs.of("server"),log), "bad method name");
    }

    DatabaseMetaHandler newHandler() {
        DBServersStore serversStore = MemoryDBServersStore.of();
        ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
        DBMetaData.Factory db = LocalDBMetaDataFactory.of(serversStore,managedFunction,log);
        DatabaseMetaHandler handler = DatabaseMetaHandler.of(db,serversStore,managedFunction,log);
        return handler;
    }
}
