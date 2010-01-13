package com.cve.web.db;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBHintsStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.cve.util.URIs;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class SelectBuilderHandlerTest {

    DBServer server = DBServer.uri(URIs.of("server"));
    DBServersStore serversStore = MemoryDBServersStore.of();
    DBHintsStore hintsStore = MemoryDBHintsStore.of();
    ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    DBMetaData.Factory db = LocalDBMetaDataFactory.of(serversStore,managedFunction);
    SelectBuilderHandler handler = SelectBuilderHandler.of(db,serversStore,hintsStore);
    {
        DBServer server = DBServer.uri(URIs.of("server"));
        serversStore.put(server, DBConnectionInfo.NULL);
    }

    @Test
    public void getResults() throws SQLException {
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:"));
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL, "", "");
        String            uri = "//server/INFORMATION_SCHEMA/INFORMATION_SCHEMA.CATALOGS/INFORMATION_SCHEMA.CATALOGS.CATALOG_NAME/";
        SelectResults results = handler.getResultsFromDB(uri);
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
