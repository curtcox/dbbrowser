package com.cve.io.db;

import com.cve.io.db.driver.DefaultDBMetaData;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.Database;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.DBServer;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.cve.util.URIs;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class DBMetaDataIOTest {

    @Test
    public void getTablesOnServerDatabase() throws SQLException {
        DBServer server = DBServer.uri(URIs.of("server"));
        Database database = server.databaseName("DB1");
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:"));
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL, "", "");
        DBServersStore  store = MemoryDBServersStore.of();
        store.put(server, info);
        DefaultDBConnection connection = DefaultDBConnection.of(info,null,null);
        DBMetaData meta = DefaultDBMetaData.getDbmd(connection,null,null);
        assertEquals(0,meta.getTablesOn(database).value.size());
    }


}
