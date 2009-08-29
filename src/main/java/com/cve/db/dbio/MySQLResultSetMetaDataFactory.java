package com.cve.db.dbio;

import com.cve.db.Server;
import java.sql.ResultSetMetaData;

/**
 *
 * @author Curt
 */
final class MySQLResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public MySQLResultSetMetaDataFactory(Server server, ResultSetMetaData meta) {
        super(server,meta);
    }

}
