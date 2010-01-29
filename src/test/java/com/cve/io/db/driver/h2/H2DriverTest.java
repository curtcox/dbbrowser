package com.cve.io.db.driver.h2;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.DefaultDBConnection;
import com.cve.io.db.NoResultSetMetaData;
import com.cve.io.db.SelectRenderer;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.log.Log;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.sample.db.SampleH2Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.cve.util.URIs;
import java.sql.ResultSetMetaData;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class H2DriverTest {

    final DBServer server = SampleH2Server.SAMPLE;
    final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    final DBServersStore serversStore = MemoryDBServersStore.of();
    final DBConnectionInfo info = SampleH2Server.getConnectionInfo();
    final DefaultDBConnection connection = DefaultDBConnection.of(info,serversStore,managedFunction);
    final H2Driver driver = H2Driver.of( managedFunction,serversStore);
    final DBMetaData dbmd = driver.getDBMetaData(connection);


    @Test
    public void getJDBCURL() {
        String name = "foo";
        JDBCURL url = driver.getJDBCURL(name);
        assertEquals(JDBCURL.uri(URIs.of("jdbc:h2:foo")),url);
    }

    @Test
    public void getDBMetaData() {
        DBMetaData meta = driver.getDBMetaData(connection);
        assertTrue(meta.getColumnsFor(server).value.size() > 350);
        assertEquals(1,meta.getDatabasesOn(server).value.size());
    }

    @Test
    public void getSelectRenderer() {
        SelectRenderer renderer = driver.getSelectRenderer();
        assertNotNull(renderer);
    }

    @Test
    public void getResultSetFactory() {
        ResultSetMetaData meta = new NoResultSetMetaData() {
            @Override public int    getColumnCount()           { return 1; }
            @Override public String getSchemaName(int column)  { return ""; }
            @Override public String getTableName(int column)   { return ""; }
            @Override public String getColumnName(int column)  { return ""; }
            @Override public String getCatalogName(int column) { return ""; }
        };
        final DBResultSetMetaDataIO metaIO = DBResultSetMetaDataIO.of(meta);
        DefaultDBResultSetMetaDataFactory factory = driver.getResultSetFactory(server, metaIO);
        assertNotNull(factory);
    }

    public static void main(String[] args) {
        H2DriverTest test = new H2DriverTest();
        test.getJDBCURL();
        test.getResultSetFactory();
        test.getSelectRenderer();
        test.getDBMetaData();
    }
}
