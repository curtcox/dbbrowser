package com.cve.db.dbio;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * Low-level object for interacting with a result set.
 * @author curt
 */
@Immutable
public final class DBResultSetIO {

    public final DBResultSetMetaDataIO meta;
    public final ImmutableList<ImmutableMap> rows;

    /**
     * Use the factory.
     */
    private DBResultSetIO(DBResultSetMetaDataIO meta, ImmutableList<ImmutableMap> rows) {
        this.meta = meta;
        this.rows = rows;
    }

    public static DBResultSetIO of(ResultSet results) {
        try {
            DBResultSetMetaDataIO meta = DBResultSetMetaDataIO.of(results);
            ImmutableList<ImmutableMap> rows = readRows(results,meta);
            return new DBResultSetIO(meta,rows);
        } finally {
            try {
                results.close();
            } catch (SQLException e) {
                
            }
        }
    }

    private static ImmutableList<ImmutableMap> readRows(ResultSet results, DBResultSetMetaDataIO meta) {
        List rows = Lists.newArrayList();
        return ImmutableList.copyOf(rows);
    }

    public int getInt(int rowNumber, int columnNumber) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    int getInt(int rowNumber, String columnName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    String getString(int rowNumber, String columnName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    String getString(int rowNumber, int columnNumber) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Get the object from the specified column.
     * Return a string describing the conversion error if one is encountered.
     */
    private static Object getObject(ResultSet results, int c) throws SQLException {
        try {
            return results.getObject(c);
        } catch (SQLException e) {
            ResultSetMetaData meta = results.getMetaData();
            String        typeName = meta.getColumnTypeName(c);
            String       className = meta.getColumnClassName(c);
            return "Error converting " + typeName + "/" + className;
        }
    }


}
