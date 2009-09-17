package com.cve.db.dbio.driver;

import com.cve.db.Server;
import java.sql.ResultSetMetaData;

/**
 *
 * @author Curt
 */
final class MsSqlTdsResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public MsSqlTdsResultSetMetaDataFactory(Server server, ResultSetMetaData meta) {
        super(server,meta);
    }

}
