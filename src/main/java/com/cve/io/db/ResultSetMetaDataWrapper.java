package com.cve.io.db;

import com.cve.util.Check;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Wraps one meta data to provide another.
 * This shows what methods we actually use, and provides points to
 * intercept and debug.
 * @author curt
 */
public final class ResultSetMetaDataWrapper extends NoResultSetMetaData {

    private final ResultSetMetaData meta;

    private ResultSetMetaDataWrapper(ResultSetMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static ResultSetMetaData of(ResultSetMetaData meta) {
        return new ResultSetMetaDataWrapper(meta);
    }

    @Override
    public int getColumnCount() throws SQLException {
        return meta.getColumnCount();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return meta.getColumnName(column);
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return meta.getCatalogName(column);
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return meta.getSchemaName(column);
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return meta.getTableName(column);
    }

}
