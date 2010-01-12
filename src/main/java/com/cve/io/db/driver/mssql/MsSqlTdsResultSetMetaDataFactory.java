package com.cve.io.db.driver.mssql;

import com.cve.model.db.DBServer;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;

/**
 *
 * @author Curt
 */
final class MsSqlTdsResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public MsSqlTdsResultSetMetaDataFactory(DBServer server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
