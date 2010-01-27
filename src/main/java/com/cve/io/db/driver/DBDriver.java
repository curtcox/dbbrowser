package com.cve.io.db.driver;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.web.Search;

/**
 * Database drivers that we support.
 * @author curt
 */
public class DBDriver implements DriverIO {

    @Override
    public JDBCURL getJDBCURL(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DBMetaData getDBMetaData(DBConnection connection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SQL renderCount(Select select, Search search) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public SQL render(Select select, Search search) {
        throw new UnsupportedOperationException("Not yet implemented");
    }



}
