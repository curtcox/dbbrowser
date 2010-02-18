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
import com.cve.web.core.Search;

/**
 *
 * @author curt
 */
public final class CompositeDBDriver implements DBDriver {

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
