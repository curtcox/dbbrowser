package com.cve.io.db.driver.h2;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.io.db.driver.DriverIO;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
public final class H2Driver implements DriverIO {

    final Log log;
    final ManagedFunction.Factory managedFunction;
    final DBServersStore serversStore;

    public final class Factory implements DriverIO.Factory {

        final Log log;

        public Factory(Log log) {
            this.log = notNull(log);
        }

        @Override
        public DriverIO of(Log log, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
            return H2Driver.of(log,managedFunction,serversStore);
        }

    }

    private H2Driver(Log log, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        this.log = notNull(log);
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
    }

    public static H2Driver of(Log log, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return new H2Driver(log,managedFunction,serversStore);
    }
    
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:h2:" + name;
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBConnection connection) {
        return H2MetaData.of(connection,managedFunction,serversStore,log);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return H2SelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return H2ResultSetMetaDataFactory.of(server, meta,log);
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection) {
        return H2MetaDataIO.of(connection, managedFunction,log);
    }


}
