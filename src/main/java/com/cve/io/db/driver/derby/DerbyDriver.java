package com.cve.io.db.driver.derby;

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
public final class DerbyDriver implements DriverIO {

    private DerbyDriver() {}

    public static DerbyDriver of() {
        return new DerbyDriver();
    }
    
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:h2:" + name;
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBMetaDataIO dbmd, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return DerbyMetaData.of(dbmd,managedFunction,serversStore);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return DerbySelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new DerbyResultSetMetaDataFactory(server, meta);
    }


}
