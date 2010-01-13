package com.cve.stores.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.DBServer;
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
        ImmutableList<DBServer> servers = MemoryDBServersStore.of().keys();
        assertNotNull(servers);
    }

    @Test
    public void getConnection() {
        DBServer server = DBServer.uri(URIs.of("server"));
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:db1"));
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL,"","");
        DBServersStore  store = MemoryDBServersStore.of();
        DBConnectionInfo info2 = store.get(server);
        assertEquals(info,info2);
    }

}
