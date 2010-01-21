package com.cve.io.db;

import com.cve.log.Log;
import com.cve.util.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Immutable low-level value object for interacting with a result set.
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
        DBResultSetMetaDataIO meta = DBResultSetMetaDataIO.of(new NullResultSetMetaData());
        ImmutableList<ImmutableMap> rows = ImmutableList.of();
        Log log = null;
        return new DBResultSetIO(meta,rows,log);
    }

    /**
     * For getting values out of result sets
     */
    public static final class Getter {
        public enum Type {
            OBJECT,STRING,INT
        }

        /**
         * Column value type
         */
        public final Type type;

        /**
         * Column name or index
         */
        public final Object key;

        private Getter(Type type, Object key) {
            this.type = type;
            this.key = key;
        }

        public static Getter integer(int key) { return new Getter(Type.INT,key); }
        public static Getter integer(String key) { return new Getter(Type.INT,key); }
        public static Getter string(String key) { return new Getter(Type.STRING,key);     }
        public static Getter string(int key) { return new Getter(Type.STRING,key);     }
    }

    private static class NullResultSet extends NoResultSet {
        @Override public void close() throws SQLException {}
        @Override public ResultSetMetaData getMetaData() {
            return new NullResultSetMetaData();
        }

    }

    private static class NullResultSetMetaData extends NoResultSetMetaData {

            NullResultSetMetaData() {}

            @Override
            public int getColumnCount() {
                return 0;
            }
        }

    /**
     * Use the factory.
     */
    private DBResultSetIO(DBResultSetMetaDataIO meta, ImmutableList<ImmutableMap> rows, Log log) {
        this.meta = notNull(meta);
        this.rows = notNull(rows);
        this.log = notNull(log);
    }

    public static DBResultSetIO of(DBResultSetMetaDataIO meta, ImmutableList<ImmutableMap> rows, Log log) {
        return new DBResultSetIO(meta,rows,log);
    }

    public static DBResultSetIO of(ResultSet results, Log log) {
        try {
            DBResultSetIO resultsIO = doOf(results,log);
            return resultsIO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DBResultSetIO doOf(ResultSet results, Log log) throws SQLException {
        try {
            DBResultSetMetaDataIO meta = DBResultSetMetaDataIO.of(results.getMetaData());
            ImmutableList<ImmutableMap> rows = readRows(results,meta,log);
            return new DBResultSetIO(meta,rows,log);
        } finally {
            results.close();
        }
    }

    public static DBResultSetIO ofUsing(ResultSet results, Log log, Getter... getters) {
        return ofUsing(results,ImmutableList.of(getters),log);
    }

    public static DBResultSetIO ofUsing(ResultSet results, ImmutableList<Getter> getters, Log log) {
        try {
            DBResultSetIO resultsIO = doOfUsing(results,getters,log);
            return resultsIO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DBResultSetIO doOfUsing(ResultSet results, ImmutableList<Getter> getters, Log log) throws SQLException {
        try {
            DBResultSetMetaDataIO meta = DBResultSetMetaDataIO.of(results.getMetaData());
            ImmutableList<ImmutableMap> rows = readRows(results,meta,log);
            while (results.next()) {
                for (Getter getter : getters) {
                }
            }
            return new DBResultSetIO(meta,rows,log);
        } finally {
            results.close();
        }
    }

    private static ImmutableList<ImmutableMap> readRows(ResultSet results, DBResultSetMetaDataIO meta, Log log) throws SQLException {
        List rows = Lists.newArrayList();
        int cols = meta.columnCount;
        while (results.next()) {
            Map row = Maps.newHashMap();
            for (int c=1; c<=cols; c++) {
                Object v = getObject(results,c,log);
                log.debug(c + "->" + v);
                if (v!=null) {
                    row.put(c-1,v);
                    row.put(meta.columnNames.get(c-1), v);
                }
            }
            rows.add(ImmutableMap.copyOf(row));
        }
        return ImmutableList.copyOf(rows);
    }

    public long getLong(int rowNumber, int columnNumber) {
        Object value = rows.get(rowNumber).get(columnNumber);
        if (value instanceof Long)    { return (Long)    value; }
        if (value instanceof Integer) { return (Integer) value; }
        throw new IllegalArgumentException(value + " can't be converted to a long");
    }

    public long getLong(int rowNumber, String columnName) {
        Object value = rows.get(rowNumber).get(columnName);
        if (value instanceof Long)    { return (Long)    value; }
        if (value instanceof Integer) { return (Integer) value; }
        throw new IllegalArgumentException(value + " can't be converted to a long");
    }

    public String getString(int rowNumber, String columnName) {
        return (String) rows.get(rowNumber).get(columnName);
    }

    public String getString(int rowNumber, int columnNumber) {
        return (String) rows.get(rowNumber).get(columnNumber);
    }

    /**
     * Get the object from the specified column.
     * Return a string describing the conversion error if one is encountered.
     */
    private static Object getObject(ResultSet results, int c, Log log) throws SQLException {
        try {
            return results.getObject(c);
        } catch (SQLException e) {
            ResultSetMetaData meta = results.getMetaData();
            String        typeName = meta.getColumnTypeName(c);
            String       className = meta.getColumnClassName(c);
            String message = "Error converting column " + c + " type/class="+ typeName + "/" + className;
            e.printStackTrace();
            log.info(message);
            log.warn(e);
            return message;
        }
    }

    @Override
    public String toString() {
        return "<DBResultSetIO>" +
                " <meta>" + Strings.first(1000,meta.toString()) + "</meta>" +
                " <rows>" + Strings.first(1000,rows.toString()) + "</rows>" +
               "</DBResultSetIO>";
    }

    /**
     * Logging stuff.
     */
    final Log log;
    private void info(String mesage) { log.info(mesage);  }
    private void debug(String mesage) { log.debug(mesage);  }
}
