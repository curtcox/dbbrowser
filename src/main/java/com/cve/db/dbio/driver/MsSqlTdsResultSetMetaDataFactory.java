package com.cve.db.dbio.driver;

import com.cve.db.Server;
import com.cve.db.dbio.DBResultSetMetaDataIO;

/**
 *
 * @author Curt
 */
final class MsSqlTdsResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public MsSqlTdsResultSetMetaDataFactory(Server server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
