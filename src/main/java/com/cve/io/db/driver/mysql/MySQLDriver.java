package com.cve.io.db.driver.mysql;

import com.cve.io.db.DBConnection;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBMetaDataIO;
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
public final class MySQLDriver implements DBDriver {

    final ManagedFunction.Factory managedFunction;
    final DBServersStore serversStore;
    final Log log = Logs.of();
    
    private MySQLDriver(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
        
    }

    public static DBDriver of(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return new MySQLDriver(managedFunction,serversStore);
    }

    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:mysql://" + name + ":3306";
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBConnection connection) {
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
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection) {
        return MySQLMetaDataIO.of(connection, managedFunction);
    }

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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
