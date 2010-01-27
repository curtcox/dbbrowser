package com.cve.stores.db;

import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
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

    Log log;

    @Test
    public void getServers() {
        ImmutableList<DBServer> servers = MemoryDBServersStore.of().keys();
        assertNotNull(servers);
    }

    @Test
    public void getConnection() {
        DBServer server = DBServer.uri(URIs.of("server"),log);
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:db1"));
        DBDriver driver = null;
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL,"","",driver,log);
        DBServersStore  store = MemoryDBServersStore.of();
        DBConnectionInfo info2 = store.get(server);
        assertEquals(info,info2);
    }

}
