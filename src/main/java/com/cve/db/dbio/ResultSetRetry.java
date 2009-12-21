package com.cve.db.dbio;

import com.cve.log.Log;
import com.cve.util.Check;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.cve.log.Log.args;
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
    private final DefaultDBConnection connection;

    /**
     * This knows about the connection and will generate a result set.
     */
    private final ResultSetGenerator generator;

    private ResultSetRetry(DefaultDBConnection connection, ResultSetGenerator generator) {
        this.connection = Check.notNull(connection);
        this.generator  = Check.notNull(generator);
    }

    private static ResultSetRetry of(DefaultDBConnection connection, ResultSetGenerator generator) {
        args(connection,generator);
        return new ResultSetRetry(connection,generator);
    }

    static ResultSet run(DefaultDBConnection connection, ResultSetGenerator generator) throws SQLException {
        args(connection,generator);
        return of(connection,generator).generate();
    }

    private ResultSet generate() throws SQLException {
        try {
            return generator.generate();
        } catch (SQLException e) {
            warn(e);
            connection.reset();
            return generator.generate();
        }
    }

    private static Log LOG = Log.of(ResultSetRetry.class);

    static void warn(Throwable t) {
        LOG.warn(t);
    }
}
