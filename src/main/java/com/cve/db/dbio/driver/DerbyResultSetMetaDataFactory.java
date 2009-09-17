package com.cve.db.dbio.driver;

import com.cve.db.Server;
import java.sql.ResultSetMetaData;

/**
 *
 * @author Curt
 */
final class DerbyResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public DerbyResultSetMetaDataFactory(Server server, ResultSetMetaData meta) {
        super(server,meta);
    }

}
