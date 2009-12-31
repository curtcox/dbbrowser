package com.cve.db.dbio;

import java.sql.ResultSet;

/**
 * Low-level access to result set meta data.
 * @author curt
 */
public final class DBResultSetMetaDataIO {

    static DBResultSetMetaDataIO of(ResultSet results) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Use the factory
     */
    private DBResultSetMetaDataIO() {}

    public int getColumnCount() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getSchemaName(int i) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getTableName(int i) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getColumnName(int i) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getCatalogName(int i) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
