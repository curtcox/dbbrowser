package com.cve.io.db.driver.derby;

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
public final class DerbyDriver implements DBDriver {

    final ManagedFunction.Factory managedFunction;
    final DBServersStore serversStore;
    final Log log;

    private DerbyDriver(ManagedFunction.Factory managedFunction,  DBServersStore serversStore, Log log) {
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
        this.log = notNull(log);
    }

    public static DerbyDriver of(ManagedFunction.Factory managedFunction,  DBServersStore serversStore, Log log) {
        return new DerbyDriver(managedFunction,serversStore,log);
    }
    
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:h2:" + name;
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBConnection dbmd) {
        return DerbyMetaData.of(dbmd,managedFunction,serversStore,log);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return DerbySelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new DerbyResultSetMetaDataFactory(server, meta,log);
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection) {
        throw new UnsupportedOperationException("Not supported yet.");
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
