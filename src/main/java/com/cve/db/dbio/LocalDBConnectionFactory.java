package com.cve.db.dbio;

import com.cve.db.Server;

/**
 *
 * @author curt
 */
public final class LocalDBConnectionFactory implements DBMetaData.Factory {

    @Override
    public DBMetaData of(Server server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
