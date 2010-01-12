package com.cve.io.db.driver.h2;

import com.cve.model.db.DBServer;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;

/**
 *
 * @author Curt
 */
final class H2ResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public H2ResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
