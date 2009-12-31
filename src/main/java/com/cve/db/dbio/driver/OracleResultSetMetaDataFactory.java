package com.cve.db.dbio.driver;

import com.cve.db.Server;
import com.cve.db.dbio.DBResultSetMetaDataIO;

/**
 *
 * @author Curt
 */
final class OracleResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public OracleResultSetMetaDataFactory(Server server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
