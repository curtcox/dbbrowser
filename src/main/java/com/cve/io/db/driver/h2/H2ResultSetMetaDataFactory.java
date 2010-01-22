package com.cve.io.db.driver.h2;

import com.cve.model.db.DBServer;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.log.Log;

/**
 *
 * @author Curt
 */
final class H2ResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    private H2ResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta, Log log) {
        super(server,meta,log);
    }

    static H2ResultSetMetaDataFactory of(DBServer server, DBResultSetMetaDataIO meta, Log log) {
        return new H2ResultSetMetaDataFactory(server, meta,log);
    }
}
