package com.cve.web.db;

import com.cve.db.ConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.stores.Stores;
import com.cve.util.URIs;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class SelectBuilderHandlerTest {

    @Test
    public void getResults() throws SQLException {
        Server server = Server.uri(URIs.of("server"));
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:"));
        ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcURL, "", "");
        Stores.getServerStores().addServer(server, info);

        String            uri = "//server/INFORMATION_SCHEMA/INFORMATION_SCHEMA.CATALOGS/INFORMATION_SCHEMA.CATALOGS.CATALOG_NAME/";
        SelectResults results = SelectBuilderHandler.of(null).getResultsFromDB(uri);
        assertNotNull(results);
    }

    @Test
    public void isSelectBuilderRequest() {
        assertFalse(SelectBuilderHandler.isSelectBuilderRequest("//"));
        assertFalse(SelectBuilderHandler.isSelectBuilderRequest("//server/"));
        assertFalse(SelectBuilderHandler.isSelectBuilderRequest("//server/db/"));
        assertTrue(SelectBuilderHandler.isSelectBuilderRequest("//server/db/table/"));
    }
}
