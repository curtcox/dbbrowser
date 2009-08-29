package com.cve.db.dbio;

import com.cve.db.Server;
import java.sql.ResultSetMetaData;

/**
 *
 * @author Curt
 */
final class OracleResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public OracleResultSetMetaDataFactory(Server server, ResultSetMetaData meta) {
        super(server,meta);
    }

}
