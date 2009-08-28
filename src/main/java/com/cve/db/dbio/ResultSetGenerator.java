
package com.cve.db.dbio;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Something that generates a result set.
 * This interface exists to deal with stale connections.
 * A generator can be wrapped with logic to get a new connection and retry.
 * @author curt
 */
interface ResultSetGenerator {

    ResultSet generate() throws SQLException;
}
