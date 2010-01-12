package com.cve.web.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBServer;
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
        DBServer server = DBServer.uri(URIs.of("server"));
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:"));
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL, "", "");
        Stores stores = null;
        stores.getStore(null).put(server, info);

        String            uri = "//server/INFORMATION_SCHEMA/INFORMATION_SCHEMA.CATALOGS/INFORMATION_SCHEMA.CATALOGS.CATALOG_NAME/";
        SelectResults results = SelectBuilderHandler.of(null,null,null).getResultsFromDB(uri);
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
