package com.cve.io.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Low-level object for interacting with a result set.
 * @author curt
 */
@Immutable
public final class DBResultSetIO {

    /**
     * The meta-data from the result set.
     */
    public final DBResultSetMetaDataIO meta;

    /**
     * The rows and columns from the result set.
     */
    public final ImmutableList<ImmutableMap> rows;

    /**
     * Use this in place of null.
     */
    public static final DBResultSetIO NULL = newNull();

    private static DBResultSetIO newNull() {
        DBResultSetMetaDataIO meta = DBResultSetMetaDataIO.of(new NullResultSet());
        ImmutableList<ImmutableMap> rows = ImmutableList.of();
        return new DBResultSetIO(meta,rows);
    }

    public final class Getter {
    }

    private static class NullResultSet extends NoResultSet {
        @Override public void close() throws SQLException {}
        @Override public ResultSetMetaData getMetaData() {
            return new NoResultSetMetaData() {
                @Override  public int getColumnCount() { return 0; }
            };
        }
    }

    /**
     * Use the factory.
     */
    private DBResultSetIO(DBResultSetMetaDataIO meta, ImmutableList<ImmutableMap> rows) {
        this.meta = notNull(meta);
        this.rows = notNull(rows);
    }

    public static DBResultSetIO of(DBResultSetMetaDataIO meta, ImmutableList<ImmutableMap> rows) {
        return new DBResultSetIO(meta,rows);
    }

    public static DBResultSetIO of(ResultSet results) {
        try {
            DBResultSetMetaDataIO meta = DBResultSetMetaDataIO.of(results);
            ImmutableList<ImmutableMap> rows = readRows(results,meta);
            while (results.next()) {
            }
            return new DBResultSetIO(meta,rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                results.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DBResultSetIO of(ResultSet results, ImmutableList<Getter> getters) {
        try {
            DBResultSetMetaDataIO meta = DBResultSetMetaDataIO.of(results);
            ImmutableList<ImmutableMap> rows = readRows(results,meta);
            while (results.next()) {
                for (Getter getter : getters) {
                }
            }
            return new DBResultSetIO(meta,rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                results.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static ImmutableList<ImmutableMap> readRows(ResultSet results, DBResultSetMetaDataIO meta) {
        List rows = Lists.newArrayList();
        return ImmutableList.copyOf(rows);
    }

    public int getInt(int rowNumber, int columnNumber) {
        return (Integer) rows.get(rowNumber).get(columnNumber);
    }

    int getInt(int rowNumber, String columnName) {
        return (Integer) rows.get(rowNumber).get(columnName);
    }

    String getString(int rowNumber, String columnName) {
        return (String) rows.get(rowNumber).get(columnName);
    }

    String getString(int rowNumber, int columnNumber) {
        return (String) rows.get(rowNumber).get(columnNumber);
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
