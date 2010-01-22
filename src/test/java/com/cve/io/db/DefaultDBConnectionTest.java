package com.cve.io.db;

import com.cve.io.db.driver.DefaultDBMetaData;
import com.cve.io.db.driver.h2.H2Driver;
import com.cve.log.Log;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.Database;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.DBServer;
import com.cve.sample.db.DBSampleServerTestObjects;
import com.cve.sample.db.SampleGeoDB;
import com.cve.sample.db.SampleH2Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
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
public class DefaultDBConnectionTest {

    Log log;
    static final DBServer server = DBServer.uri(URIs.of("server"));
    static final Database database = server.databaseName("DB1");
    static final DBServer sampleServer = SampleH2Server.SAMPLE;
    final Database geoDatabase = SampleGeoDB.GEO;
    final DBMetaData sampleMetaData = DBSampleServerTestObjects.meta;

    DBMetaData newEmpty() {
        JDBCURL jdbcURL = JDBCURL.uri(URIs.of("jdbc:h2:mem:"));
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL, "", "");
        DBServersStore  store = MemoryDBServersStore.of();
        store.put(server, info);
        DBServersStore serversStore = MemoryDBServersStore.of();
        ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
        DefaultDBConnection connection = DefaultDBConnection.of(info,serversStore,managedFunction,log);
        DBMetaData meta = H2Driver.of().getDBMetaData(connection,managedFunction,serversStore,log);

        return meta;
    }

    @Test
    public void getTablesOnEmptyServerDatabase() throws SQLException {
        assertEquals(0,newEmpty().getTablesOn(database).value.size());
    }

    @Test
    public void getDatabasessOnEmptyServerDatabase() throws SQLException {
        assertEquals(0,newEmpty().getDatabasesOn(server).value.size());
    }

    @Test
    public void getColumnsForEmptyServerDatabase() throws SQLException {
        assertEquals(0,newEmpty().getColumnsFor(server).value.size());
    }

    @Test
    public void getTablesOnSampleServerDatabase() throws SQLException {
        assertEquals(3,sampleMetaData.getTablesOn(geoDatabase).value.size());
    }

    @Test
    public void getDatabasessOnSampleServerDatabase() throws SQLException {
        assertEquals(2,sampleMetaData.getDatabasesOn(sampleServer).value.size());
    }

    @Test
    public void getColumnsForSampleServerDatabase() throws SQLException {
        assertEquals(4,sampleMetaData.getColumnsFor(sampleServer).value.size());
    }

    public static void main(String[] args) throws SQLException {
        DefaultDBConnectionTest test = new DefaultDBConnectionTest();
        test.sampleMetaData.getDatabasesOn(sampleServer);
        // test.getTablesOnSampleServerDatabase();
    }
}
