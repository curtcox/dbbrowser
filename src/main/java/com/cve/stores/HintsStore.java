package com.cve.stores;

import com.cve.db.DBColumn;
import com.cve.db.Hints;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;

/**
 * The {@link Hints} we know about.
 * There are several sources for hints.
 * <ol>
 * <li> Foreign key relationships defined on the database.
 * <li> Hints we have put in the store "manually" -- like with a hint file.
 * <li> Hints we have mined from:
 *    <ol>
 *        <li> the existing database data
 *        <li> SQL in log files or source code
 *    </ol>
 * </ol>
 */
public interface HintsStore {

    Hints getHints(ImmutableList<DBColumn> columns) throws SQLException;

}
