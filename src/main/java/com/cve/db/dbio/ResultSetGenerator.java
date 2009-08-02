
package com.cve.db.dbio;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author curt
 */
interface ResultSetGenerator {

    ResultSet generate() throws SQLException;
}
