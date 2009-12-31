package com.cve.db.dbio.driver;

import com.cve.db.Server;
import com.cve.db.dbio.DBResultSetMetaDataIO;

/**
 *
 * @author Curt
 */
final class H2ResultSetMetaDataFactory extends DefaultDBResultSetMetaDataFactory {

    public H2ResultSetMetaDataFactory(Server server, DBResultSetMetaDataIO meta) {
        super(server,meta);
    }

}
