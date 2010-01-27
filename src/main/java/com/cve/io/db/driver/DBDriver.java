package com.cve.io.db.driver;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.log.Log;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.web.Search;

/**
 *
 * @author curt
 */
public interface DBDriver {


    public interface Factory {
        DBDriver of(Log log, ManagedFunction.Factory managedFunction, DBServersStore serversStore);
    }

    boolean handles(JDBCURL url);

    JDBCURL getJDBCURL(String name);

    DBMetaData getDBMetaData(DBConnection connection);

    SelectRenderer getSelectRenderer();

    DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta);

    DBMetaDataIO getDBMetaDataIO(DBConnection connection);

    SQL render(Select select, Search search);

    SQL renderCount(Select select, Search search);

}
