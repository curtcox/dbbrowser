
package com.cve.web.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.DBServer;
import com.cve.stores.Stores;
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

    private DBServer getStoreServer() {
        DBServer server = DBServer.uri(URIs.of("server"));
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:db"));
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL, "", "");
        Stores stores = null;
        stores.getStore(null).put(server, info);
        return server;
    }

    @Test
    public void getCatalogs() throws SQLException {
        DBServer server = getStoreServer();
        String unnamed = borderTable(tr(th("CATALOG_NAME")) + tr(td("UNNAMED")) );
        String db1 = borderTable(tr(th("CATALOG_NAME")) + tr(td("DB1")) );
        String actual = DatabaseMetaHandler.of(null,null,null).getCatalogs(server);
        assertTrue(actual.equals(unnamed) || actual.equals(db1));
    }

    @Test
    public void isDatabaseMetaRequest() {
        DatabaseMetaHandler handler = DatabaseMetaHandler.of(null,null,null);
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
        DatabaseMetaHandler handler = DatabaseMetaHandler.of(null,null,null);
        handler.tryPage(DBServer.uri(URIs.of("server")), "bad method name");
    }
}
