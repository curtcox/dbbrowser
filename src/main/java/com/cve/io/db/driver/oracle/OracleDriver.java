package com.cve.io.db.driver.oracle;

import com.cve.io.db.DBConnection;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.io.db.driver.DriverIO;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;

/**
 *
 * @author curt
 */
public final class OracleDriver implements DriverIO {

    private OracleDriver() {}

    public static OracleDriver of() {
        return new OracleDriver();
    }
    
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:h2:" + name;
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBConnection connection,ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return OracleMetaData.of(connection,managedFunction,serversStore);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return OracleSelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new OracleResultSetMetaDataFactory(server, meta);
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection, Factory managedFunction) {
        return OracleMetaDataIO.of(connection, managedFunction);
    }

}
