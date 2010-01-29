package com.cve.io.db.driver.oracle;

import com.cve.model.db.DBServer;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.log.Log;

/**
 *
 * @author Curt
 */
final class OracleResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public OracleResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
