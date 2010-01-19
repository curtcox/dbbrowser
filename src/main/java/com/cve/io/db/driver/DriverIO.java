package com.cve.io.db.driver;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.DBServersStore;

/**
 *
 * @author curt
 */
public interface DriverIO {
    
    JDBCURL getJDBCURL(String name);

    DBMetaData getDBMetaData(DBConnection connection, ManagedFunction.Factory managedFunction, DBServersStore serversStore);

    SelectRenderer getSelectRenderer();

    DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta);

    public DBMetaDataIO getDBMetaDataIO(DBConnection connection, Factory managedFunction);
}
