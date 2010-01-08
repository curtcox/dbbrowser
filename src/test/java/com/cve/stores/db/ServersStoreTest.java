package com.cve.stores.db;

import com.cve.db.DBConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.db.DBServer;
import com.cve.stores.Store;
import com.cve.stores.Stores;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class ServersStoreTest {

    @Test
    public void getServers() {
        Stores stores = null;
        ImmutableList<DBServer> servers = stores.getStore(null).keys();
        assertNotNull(servers);
    }

    @Test
    public void getConnection() {
        DBServer server = DBServer.uri(URIs.of("server"));
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:db1"));
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL,"","");
        Stores stores = null;
        stores.getStore(null).put(server, info);
        Store store = stores.getStore(null);
        //DBConnection connection = store.getConnection(server);
        //assertNotNull(connection);
    }

}
