package com.cve.stores.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBServer;
import com.cve.stores.Store;

/**
 * The database {@link Server}S we know about.
 */
public interface DBServersStore extends Store<DBServer,DBConnectionInfo> {}
