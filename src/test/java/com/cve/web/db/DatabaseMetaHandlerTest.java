
package com.cve.web.db;

import com.cve.db.ConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
import com.cve.stores.ServersStore;
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

    private Server getStoreServer() {
        Server server = Server.uri(URIs.of("server"));
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:db"));
        ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcURL, "", "");
        ServersStore.addServer(server, info);
        return server;
    }

    @Test
    public void getCatalogs() throws SQLException {
        Server server = getStoreServer();
        String unnamed = borderTable(tr(th("CATALOG_NAME")) + tr(td("UNNAMED")) );
        String db1 = borderTable(tr(th("CATALOG_NAME")) + tr(td("DB1")) );
        String actual = DatabaseMetaHandler.getCatalogs(server);
        assertTrue(actual.equals(unnamed) || actual.equals(db1));
    }

    @Test
    public void isDatabaseMetaRequest() {
        assertFalse(DatabaseMetaHandler.isDatabaseMetaRequest("/server/"));
        assertFalse(DatabaseMetaHandler.isDatabaseMetaRequest("/server:8080/"));
        assertFalse(DatabaseMetaHandler.isDatabaseMetaRequest("/server/db/"));
        assertFalse(DatabaseMetaHandler.isDatabaseMetaRequest("/server/db/foo/"));
        assertFalse(DatabaseMetaHandler.isDatabaseMetaRequest("/meaty/server/"));
        assertFalse(DatabaseMetaHandler.isDatabaseMetaRequest("/motto/server/"));
        assertTrue(DatabaseMetaHandler.isDatabaseMetaRequest("/meta/server/"));
        assertTrue(DatabaseMetaHandler.isDatabaseMetaRequest("/meta/server/catalogs/"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void badRequestThrowsException() throws SQLException {
        DatabaseMetaHandler.tryPage(Server.uri(URIs.of("server")), "bad method name");
    }
}
