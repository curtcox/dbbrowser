package com.cve.db.dbio;

import com.cve.util.Check;
import java.io.PrintStream;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Wraps one meta data to provide another.
 * This shows what methods we actually use, and provides points to
 * intercept and debug.
 * @author curt
 */
public final class ResultSetMetaDataLogger extends NoResultSetMetaData {

    final ResultSetMetaData meta;

    final PrintStream out = System.out;

    private ResultSetMetaDataLogger(ResultSetMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    static ResultSetMetaData of(ResultSetMetaData meta) {
        return new ResultSetMetaDataLogger(meta);
    }

    @Override
    public int getColumnCount() throws SQLException {
        return print(meta.getColumnCount());
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return print(meta.getColumnName(column));
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return print(meta.getCatalogName(column));
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return print(meta.getSchemaName(column));
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return print(meta.getTableName(column));
    }

    <T> T print(T t) {
        out.println(t);
        return t;
    }

}
