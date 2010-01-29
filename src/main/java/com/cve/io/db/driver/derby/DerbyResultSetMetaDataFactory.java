package com.cve.io.db.driver.derby;

import com.cve.model.db.DBServer;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.log.Log;

/**
 *
 * @author Curt
 */
final class DerbyResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public DerbyResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
