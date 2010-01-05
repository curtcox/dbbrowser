package com.cve.db.dbio.driver;

import com.cve.db.DBServer;
import com.cve.db.dbio.DBResultSetMetaDataIO;

/**
 *
 * @author Curt
 */
final class MsSqlTdsResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public MsSqlTdsResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
