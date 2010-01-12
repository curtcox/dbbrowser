package com.cve.io.db;

import com.cve.util.Check;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Wraps one meta data to provide another.
 * This shows what methods we actually use, and provides points to
 * intercept and debug.
 * @author curt
 */
public final class ResultSetWrapper extends NoResultSet {

    private final ResultSet results;

    ResultSetWrapper(ResultSet results) {
        this.results = Check.notNull(results);
    }

    public static ResultSet of(ResultSet results) {
        return new ResultSetWrapper(results);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException { return results.getMetaData();  }

    @Override
    public boolean next() throws SQLException { return results.next(); }

    @Override
    public void close() throws SQLException { results.close(); }

    @Override
    public int getInt(int index) throws SQLException { return results.getInt(index); }

    @Override
    public Object getObject(int index) throws SQLException { return results.getObject(index); }

}
