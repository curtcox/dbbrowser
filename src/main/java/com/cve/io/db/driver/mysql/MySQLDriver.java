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
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import static com.cve.util.Check.notNull;
/**
 *
 * @author curt
 */
public final class MySQLDriver implements DriverIO {

    final ManagedFunction.Factory managedFunction;
    final DBServersStore serversStore;
    final Log log;
    
    private MySQLDriver(ManagedFunction.Factory managedFunction, DBServersStore serversStore,Log log) {
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
        this.log = notNull(log);
    }

    public static DriverIO of(ManagedFunction.Factory managedFunction, DBServersStore serversStore, Log log) {
        return new MySQLDriver(managedFunction,serversStore,log);
    }

    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:mysql://" + name + ":3306";
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBConnection connection) {
        return MySQLMetaData.of(connection,managedFunction,serversStore,log);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return MySQLSelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new MySQLResultSetMetaDataFactory(server, meta,log);
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection) {
        return MySQLMetaDataIO.of(connection, managedFunction,log);
    }
}