package com.cve.stores.db;

import com.cve.db.ConnectionInfo;
import com.cve.db.Server;
import com.cve.stores.Store;

/**
 * The database {@link Server}S we know about.
 */
public interface ServersStore extends Store<Server,ConnectionInfo> {

}
