package com.cve.stores;

import com.cve.db.dbio.DBConnection;
import com.cve.db.ConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
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
        ImmutableList<Server> servers = Stores.getServerStores().getServers();
        assertNotNull(servers);
    }

    @Test
    public void getConnection() {
        Server server = Server.uri(URIs.of("server"));
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:db1"));
        ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcURL,"","");
        Stores.getServerStores().addServer(server, info);

        DBConnection connection = Stores.getServerStores().getConnection(server);
        assertNotNull(connection);
    }

}
