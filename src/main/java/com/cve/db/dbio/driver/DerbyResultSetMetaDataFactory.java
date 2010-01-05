package com.cve.db.dbio.driver;

import com.cve.db.DBServer;
import com.cve.db.dbio.DBResultSetMetaDataIO;

/**
 *
 * @author Curt
 */
final class DerbyResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public DerbyResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
