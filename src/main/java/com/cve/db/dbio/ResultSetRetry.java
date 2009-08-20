package com.cve.db.dbio;

import com.cve.util.Check;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * For retrying result set generators.
 * Right now, use of this class allows replacing a nested try with a marginally
 * better inner class.
 * The big advantage is that it keeps the retry logic in one place.
 */
public final class ResultSetRetry {

    /**
     * The connection we want to try and retry on
     */
    private final DBConnection connection;

    /**
     * This knows about the connection and will generate a result set.
     */
    private final ResultSetGenerator generator;

    private ResultSetRetry(DBConnection connection, ResultSetGenerator generator) {
        this.connection = Check.notNull(connection);
        this.generator  = Check.notNull(generator);
    }

    private static ResultSetRetry of(DBConnection connection, ResultSetGenerator generator) {
        return new ResultSetRetry(connection,generator);
    }

    static ResultSet run(DBConnection connection, ResultSetGenerator generator) throws SQLException {
        return of(connection,generator).generate();
    }

    private ResultSet generate() throws SQLException {
        try {
            return generator.generate();
        } catch (SQLException e) {
            connection.reset();
            return generator.generate();
        }
    }
}
