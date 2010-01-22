package com.cve.io.db.driver.mssql;

import com.cve.model.db.DBServer;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.log.Log;

/**
 *
 * @author Curt
 */
final class MsSqlTdsResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public MsSqlTdsResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta, Log log) {
        super(server,meta,log);
    }

}
