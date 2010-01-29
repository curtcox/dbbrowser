package com.cve.io.db.driver.h2;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import static com.cve.util.Check.notNull;
import com.cve.web.Search;

/**
 *
 * @author curt
 */
public final class H2Driver implements DBDriver {

    final Log log = Logs.of();
    final ManagedFunction.Factory managedFunction;
    final DBServersStore serversStore;

    @Override
    public SQL render(Select select, Search search) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SQL renderCount(Select select, Search search) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean handles(JDBCURL url) {
        return url.toString().startsWith("jdbc:h2:");
    }

    public static final class Factory implements DBDriver.Factory {

        final Log log = Logs.of();

        public Factory() {}

        public static Factory of() {
            return new Factory();
        }

        @Override
        public DBDriver of(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
            return H2Driver.of(managedFunction,serversStore);
        }

    }

    private H2Driver(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
    }

    public static H2Driver of(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return new H2Driver(managedFunction,serversStore);
    }
    
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:h2:" + name;
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBConnection connection) {
        return H2MetaData.of(connection,managedFunction,serversStore);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return H2SelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return H2ResultSetMetaDataFactory.of(server, meta);
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection) {
        return H2MetaDataIO.of(connection, managedFunction);
    }


}
