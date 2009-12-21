
package com.cve.db.dbio;

import com.cve.db.ConnectionInfo;
import com.cve.db.SQL;
import com.cve.db.Server;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * A database connection.
 * Connection reuse is essential for speed, since connections take so long
 * to establish.  Pooling, however probably isn't necessary.
 * <p>
 * Since connections can timeout, we use a factory that knows how to create them,
 * and then recreate them as needed.
 */
public interface DBConnection {

    ConnectionInfo getInfo();

    DBMetaData getMetaData();

    DBResultSetMetaData getMetaData(Server server, ResultSet results) throws SQLException;


    /**
     * Execute the given SQL.
     */
    ResultSet select(final SQL sql) throws SQLException;


}
