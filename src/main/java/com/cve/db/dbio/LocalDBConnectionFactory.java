package com.cve.db.dbio;

import com.cve.db.DBServer;

/**
 *
 * @author curt
 */
public final class LocalDBConnectionFactory implements DBMetaData.Factory {

    @Override
    public DBMetaData of(DBServer server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
