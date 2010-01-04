package com.cve.db.dbio;

import com.cve.db.dbio.driver.DefaultDBMetaData;
import com.cve.db.ConnectionInfo;
import com.cve.db.Database;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
import com.cve.stores.Stores;
import com.cve.stores.db.ServersStore;
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
        Server server = Server.uri(URIs.of("server"));
        Database database = server.databaseName("DB1");
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:"));
        ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcURL, "", "");
        Stores stores = null;
        ServersStore serversStore = null;
        serversStore.put(server, info);
        DBConnection connection = DefaultDBConnection.of(info,null,null);
        DBMetaData meta = DefaultDBMetaData.getDbmd(connection,null,null);
        assertEquals(0,meta.getTablesOn(database).value.size());
    }


}
