package com.cve.io.db;

import com.cve.model.db.DBServer;

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
