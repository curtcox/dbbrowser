package com.cve.io.db.driver.mysql;

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
public final class MySQLDriver implements DriverIO {

    private MySQLDriver() {}

    public static DriverIO of() {
        return new MySQLDriver();
    }

    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:mysql://" + name + ":3306";
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBConnection connection,ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return MySQLMetaData.of(connection,managedFunction,serversStore);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return MySQLSelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new MySQLResultSetMetaDataFactory(server, meta);
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection, Factory managedFunction) {
        return MySQLMetaDataIO.of(connection, managedFunction);
    }
}
