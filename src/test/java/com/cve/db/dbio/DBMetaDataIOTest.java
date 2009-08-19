package com.cve.db.dbio;

import com.cve.db.ConnectionInfo;
import com.cve.db.Database;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
import com.cve.stores.ServersStore;
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
        ServersStore.addServer(server, info);
        DBConnection connection = DBConnection.info(info);
        DBMetaData meta = DefaultDBMetaData.getDbmd(connection);
        assertEquals(0,meta.getTablesOn(database).size());
    }


}
