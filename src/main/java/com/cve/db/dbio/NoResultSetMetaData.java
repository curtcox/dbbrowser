package com.cve.db.dbio;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Not a result set meta data, but you could extend it to make one.
 * @author Curt
 */
public abstract class NoResultSetMetaData implements ResultSetMetaData {

    @Override
    public int getColumnCount() throws SQLException {        throw no();    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {        throw no();    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {        throw no();    }

    @Override
    public boolean isSearchable(int column) throws SQLException {        throw no();    }

    @Override
    public boolean isCurrency(int column) throws SQLException {        throw no();    }

    @Override
    public int isNullable(int column) throws SQLException {        throw no();    }

    @Override
    public boolean isSigned(int column) throws SQLException {        throw no();    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {        throw no();    }

    @Override
    public String getColumnLabel(int column) throws SQLException {        throw no();    }

    @Override
    public String getColumnName(int column) throws SQLException {        throw no();    }

    @Override
    public String getSchemaName(int column) throws SQLException {        throw no();    }

    @Override
    public int getPrecision(int column) throws SQLException {        throw no();    }

    @Override
    public int getScale(int column) throws SQLException {        throw no();    }

    @Override
    public String getTableName(int column) throws SQLException {        throw no();    }

    @Override
    public String getCatalogName(int column) throws SQLException {        throw no();    }

    @Override
    public int getColumnType(int column) throws SQLException {        throw no();    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {        throw no();    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {        throw no();    }

    @Override
    public boolean isWritable(int column) throws SQLException {        throw no();    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {        throw no();    }

    @Override
    public String getColumnClassName(int column) throws SQLException {        throw no();    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {        throw no();    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {        throw no();    }

    private static UnsupportedOperationException no() {
        return new UnsupportedOperationException();
    }
}
