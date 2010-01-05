
package com.cve.db.dbio;

import com.cve.db.DBConnectionInfo;
import com.cve.db.SQL;
import com.cve.db.DBServer;
import com.cve.stores.CurrentValue;

/**
 * A database connection.
 * Connection reuse is essential for speed, since connections take so long
 * to establish.  Pooling, however probably isn't necessary.
 * <p>
 * Since connections can timeout, we use a factory that knows how to create them,
 * and then recreate them as needed.
 */
public interface DBConnection {

    /**
     * Return information about this connection.
     */
    DBConnectionInfo getInfo();

    /**
     * Return information about the database this is connected to.
     */
    DBMetaData getMetaData();

    /**
     * Return information about the given result set.
     */
    DBResultSetMetaData getMetaData(DBServer server, DBResultSetIO results);


    /**
     * Execute the given SQL.  In the future, this should be changed to return
     * an immutable value class
     */
    CurrentValue<DBResultSetIO> select(final SQL sql);


}
