package com.cve.db.dbio;

import com.cve.db.Server;
import java.sql.ResultSetMetaData;

/**
 *
 * @author Curt
 */
final class H2ResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public H2ResultSetMetaDataFactory(Server server, ResultSetMetaData meta) {
        super(server,meta);
    }

}
