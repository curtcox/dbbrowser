package com.cve.io.db.driver.h2;

import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.io.db.driver.DriverIO;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;

/**
 *
 * @author curt
 */
public final class H2Driver implements DriverIO {

    private H2Driver() {}

    public static H2Driver of() {
        return new H2Driver();
    }
    
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:h2:" + name;
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBMetaDataIO dbmd, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return H2MetaData.of(dbmd,managedFunction,serversStore);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return H2SelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new H2ResultSetMetaDataFactory(server, meta);
    }


}
