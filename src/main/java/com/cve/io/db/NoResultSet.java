package com.cve.io.db;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * This is not a result set by any means, but you could extend it to
 * make one.
 * @author curt
 */
public abstract class NoResultSet implements ResultSet {


    @Override
    public boolean next() throws SQLException {        throw no();    }

    @Override
    public void close() throws SQLException {        throw no();    }

    @Override
    public boolean wasNull() throws SQLException {        throw no();    }

    @Override
    public String getString(int arg0) throws SQLException {        throw no();    }

    @Override
    public boolean getBoolean(int arg0) throws SQLException {        throw no();    }

    @Override
    public byte getByte(int arg0) throws SQLException {        throw no();    }

    @Override
    public short getShort(int arg0) throws SQLException {        throw no();    }

    @Override
    public int getInt(int arg0) throws SQLException {        throw no();    }

    @Override
    public long getLong(int arg0) throws SQLException {        throw no();    }

    @Override
    public float getFloat(int arg0) throws SQLException {        throw no();    }

    @Override
    public double getDouble(int arg0) throws SQLException {        throw no();    }

    @Override
    public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {        throw no();    }

    @Override
    public byte[] getBytes(int arg0) throws SQLException {        throw no();    }

    @Override
    public Date getDate(int arg0) throws SQLException {        throw no();    }

    @Override
    public Time getTime(int arg0) throws SQLException {        throw no();    }

    @Override
    public Timestamp getTimestamp(int arg0) throws SQLException {        throw no();    }

    @Override
    public InputStream getAsciiStream(int arg0) throws SQLException {        throw no();    }

    @Override
    public InputStream getUnicodeStream(int arg0) throws SQLException {        throw no();    }

    @Override
    public InputStream getBinaryStream(int arg0) throws SQLException {        throw no();    }

    @Override
    public String getString(String arg0) throws SQLException {        throw no();    }

    @Override
    public boolean getBoolean(String arg0) throws SQLException {        throw no();    }

    @Override
    public byte getByte(String arg0) throws SQLException {        throw no();    }

    @Override
    public short getShort(String arg0) throws SQLException {        throw no();    }

    @Override
    public int getInt(String arg0) throws SQLException {        throw no();    }

    @Override
    public long getLong(String arg0) throws SQLException {        throw no();    }

    @Override
    public float getFloat(String arg0) throws SQLException {        throw no();    }

    @Override
    public double getDouble(String arg0) throws SQLException {        throw no();    }

    @Override
    public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {        throw no();    }

    @Override
    public byte[] getBytes(String arg0) throws SQLException {        throw no();    }

    @Override
    public Date getDate(String arg0) throws SQLException {        throw no();    }

    @Override
    public Time getTime(String arg0) throws SQLException {        throw no();    }

    @Override
    public Timestamp getTimestamp(String arg0) throws SQLException {        throw no();    }

    @Override
    public InputStream getAsciiStream(String arg0) throws SQLException {        throw no();    }

    @Override
    public InputStream getUnicodeStream(String arg0) throws SQLException {        throw no();    }

    @Override
    public InputStream getBinaryStream(String arg0) throws SQLException {        throw no();    }

    @Override
    public SQLWarning getWarnings() throws SQLException {        throw no();    }

    @Override
    public void clearWarnings() throws SQLException {        throw no();    }

    @Override
    public String getCursorName() throws SQLException {        throw no();    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {        throw no();    }

    @Override
    public Object getObject(int arg0) throws SQLException {        throw no();    }

    @Override
    public Object getObject(String arg0) throws SQLException {        throw no();    }

    @Override
    public int findColumn(String arg0) throws SQLException {        throw no();    }

    @Override
    public Reader getCharacterStream(int arg0) throws SQLException {        throw no();    }

    @Override
    public Reader getCharacterStream(String arg0) throws SQLException {        throw no();    }

    @Override
    public BigDecimal getBigDecimal(int arg0) throws SQLException {        throw no();    }

    @Override
    public BigDecimal getBigDecimal(String arg0) throws SQLException {        throw no();    }

    @Override
    public boolean isBeforeFirst() throws SQLException {        throw no();    }

    @Override
    public boolean isAfterLast() throws SQLException {        throw no();    }

    @Override
    public boolean isFirst() throws SQLException {        throw no();    }

    @Override
    public boolean isLast() throws SQLException {        throw no();    }

    @Override
    public void beforeFirst() throws SQLException {        throw no();    }

    @Override
    public void afterLast() throws SQLException {        throw no();    }

    @Override
    public boolean first() throws SQLException {        throw no();    }

    @Override
    public boolean last() throws SQLException {        throw no();    }

    @Override
    public int getRow() throws SQLException {        throw no();    }

    @Override
    public boolean absolute(int arg0) throws SQLException {        throw no();    }

    @Override
    public boolean relative(int arg0) throws SQLException {        throw no();    }

    @Override
    public boolean previous() throws SQLException {        throw no();    }

    @Override
    public void setFetchDirection(int arg0) throws SQLException {        throw no();    }

    @Override
    public int getFetchDirection() throws SQLException {        throw no();    }

    @Override
    public void setFetchSize(int arg0) throws SQLException {        throw no();    }

    @Override
    public int getFetchSize() throws SQLException {        throw no();    }

    @Override
    public int getType() throws SQLException {        throw no();    }

    @Override
    public int getConcurrency() throws SQLException {        throw no();    }

    @Override
    public boolean rowUpdated() throws SQLException {        throw no();    }

    @Override
    public boolean rowInserted() throws SQLException {        throw no();    }

    @Override
    public boolean rowDeleted() throws SQLException {        throw no();    }

    @Override
    public void updateNull(int arg0) throws SQLException {        throw no();    }

    @Override
    public void updateBoolean(int arg0, boolean arg1) throws SQLException {        throw no();    }

    @Override
    public void updateByte(int arg0, byte arg1) throws SQLException {        throw no();    }

    @Override
    public void updateShort(int arg0, short arg1) throws SQLException {        throw no();    }

    @Override
    public void updateInt(int arg0, int arg1) throws SQLException {        throw no();    }

    @Override
    public void updateLong(int arg0, long arg1) throws SQLException {        throw no();    }

    @Override
    public void updateFloat(int arg0, float arg1) throws SQLException {        throw no();    }

    @Override
    public void updateDouble(int arg0, double arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {        throw no();    }

    @Override
    public void updateString(int arg0, String arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBytes(int arg0, byte[] arg1) throws SQLException {        throw no();    }

    @Override
    public void updateDate(int arg0, Date arg1) throws SQLException {        throw no();    }

    @Override
    public void updateTime(int arg0, Time arg1) throws SQLException {        throw no();    }

    @Override
    public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {        throw no();    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {        throw no();    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {        throw no();    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {        throw no();    }

    @Override
    public void updateObject(int arg0, Object arg1, int arg2) throws SQLException {        throw no();    }

    @Override
    public void updateObject(int arg0, Object arg1) throws SQLException {        throw no();    }

    @Override
    public void updateNull(String arg0) throws SQLException {        throw no();    }

    @Override
    public void updateBoolean(String arg0, boolean arg1) throws SQLException {        throw no();    }

    @Override
    public void updateByte(String arg0, byte arg1) throws SQLException {        throw no();    }

    @Override
    public void updateShort(String arg0, short arg1) throws SQLException {        throw no();    }

    @Override
    public void updateInt(String arg0, int arg1) throws SQLException {        throw no();    }

    @Override
    public void updateLong(String arg0, long arg1) throws SQLException {        throw no();    }

    @Override
    public void updateFloat(String arg0, float arg1) throws SQLException {        throw no();    }

    @Override
    public void updateDouble(String arg0, double arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBigDecimal(String arg0, BigDecimal arg1) throws SQLException {        throw no();    }

    @Override
    public void updateString(String arg0, String arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBytes(String arg0, byte[] arg1) throws SQLException {        throw no();    }

    @Override
    public void updateDate(String arg0, Date arg1) throws SQLException {        throw no();    }

    @Override
    public void updateTime(String arg0, Time arg1) throws SQLException {        throw no();    }

    @Override
    public void updateTimestamp(String arg0, Timestamp arg1) throws SQLException {        throw no();    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, int arg2) throws SQLException {        throw no();    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, int arg2) throws SQLException {        throw no();    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1, int arg2) throws SQLException {        throw no();    }

    @Override
    public void updateObject(String arg0, Object arg1, int arg2) throws SQLException {        throw no();
    }

    @Override
    public void updateObject(String arg0, Object arg1) throws SQLException {        throw no();    }

    @Override
    public void insertRow() throws SQLException {        throw no();    }

    @Override
    public void updateRow() throws SQLException {        throw no();    }

    @Override
    public void deleteRow() throws SQLException {        throw no();    }

    @Override
    public void refreshRow() throws SQLException {        throw no();    }

    @Override
    public void cancelRowUpdates() throws SQLException {        throw no();    }

    @Override
    public void moveToInsertRow() throws SQLException {        throw no();    }

    @Override
    public void moveToCurrentRow() throws SQLException {        throw no();    }

    @Override
    public Statement getStatement() throws SQLException {        throw no();    }

    @Override
    public Object getObject(int arg0, Map<String, Class<?>> arg1) throws SQLException {        throw no();    }

    @Override
    public Ref getRef(int arg0) throws SQLException {        throw no();    }

    @Override
    public Blob getBlob(int arg0) throws SQLException {        throw no();    }

    @Override
    public Clob getClob(int arg0) throws SQLException {        throw no();    }

    @Override
    public Array getArray(int arg0) throws SQLException {        throw no();    }

    @Override
    public Object getObject(String arg0, Map<String, Class<?>> arg1) throws SQLException {        throw no();    }

    @Override
    public Ref getRef(String arg0) throws SQLException {        throw no();    }

    @Override
    public Blob getBlob(String arg0) throws SQLException {        throw no();    }

    @Override
    public Clob getClob(String arg0) throws SQLException {        throw no();    }

    @Override
    public Array getArray(String arg0) throws SQLException {        throw no();    }

    @Override
    public Date getDate(int arg0, Calendar arg1) throws SQLException {        throw no();    }

    @Override
    public Date getDate(String arg0, Calendar arg1) throws SQLException {        throw no();    }

    @Override
    public Time getTime(int arg0, Calendar arg1) throws SQLException {        throw no();    }

    @Override
    public Time getTime(String arg0, Calendar arg1) throws SQLException {        throw no();    }

    @Override
    public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {        throw no();    }

    @Override
    public Timestamp getTimestamp(String arg0, Calendar arg1) throws SQLException {        throw no();    }

    @Override
    public URL getURL(int arg0) throws SQLException {        throw no();    }

    @Override
    public URL getURL(String arg0) throws SQLException {        throw no();    }

    @Override
    public void updateRef(int arg0, Ref arg1) throws SQLException {        throw no();    }

    @Override
    public void updateRef(String arg0, Ref arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBlob(int arg0, Blob arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBlob(String arg0, Blob arg1) throws SQLException {        throw no();    }

    @Override
    public void updateClob(int arg0, Clob arg1) throws SQLException {        throw no();    }

    @Override
    public void updateClob(String arg0, Clob arg1) throws SQLException {        throw no();    }

    @Override
    public void updateArray(int arg0, Array arg1) throws SQLException {        throw no();    }

    @Override
    public void updateArray(String arg0, Array arg1) throws SQLException {        throw no();    }

    @Override
    public RowId getRowId(int arg0) throws SQLException {        throw no();    }

    @Override
    public RowId getRowId(String arg0) throws SQLException {        throw no();    }

    @Override
    public void updateRowId(int arg0, RowId arg1) throws SQLException {        throw no();    }

    @Override
    public void updateRowId(String arg0, RowId arg1) throws SQLException {        throw no();    }

    @Override
    public int getHoldability() throws SQLException {        throw no();    }

    @Override
    public boolean isClosed() throws SQLException {        throw no();    }

    @Override
    public void updateNString(int arg0, String arg1) throws SQLException {        throw no();    }

    @Override
    public void updateNString(String arg0, String arg1) throws SQLException {        throw no();    }

    @Override
    public void updateNClob(int arg0, NClob arg1) throws SQLException {        throw no();    }

    @Override
    public void updateNClob(String arg0, NClob arg1) throws SQLException {        throw no();    }

    @Override
    public NClob getNClob(int arg0) throws SQLException {        throw no();    }

    @Override
    public NClob getNClob(String arg0) throws SQLException {        throw no();    }

    @Override
    public SQLXML getSQLXML(int arg0) throws SQLException {        throw no();    }

    @Override
    public SQLXML getSQLXML(String arg0) throws SQLException {        throw no();    }

    @Override
    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {        throw no();    }

    @Override
    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {        throw no();    }

    @Override
    public String getNString(int arg0) throws SQLException {        throw no();    }

    @Override
    public String getNString(String arg0) throws SQLException {        throw no();    }

    @Override
    public Reader getNCharacterStream(int arg0) throws SQLException {        throw no();    }

    @Override
    public Reader getNCharacterStream(String arg0) throws SQLException {        throw no();    }

    @Override
    public void updateNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateNCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateBlob(int arg0, InputStream arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateBlob(String arg0, InputStream arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateClob(int arg0, Reader arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateClob(String arg0, Reader arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateNClob(int arg0, Reader arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateNClob(String arg0, Reader arg1, long arg2) throws SQLException {        throw no();    }

    @Override
    public void updateNCharacterStream(int arg0, Reader arg1) throws SQLException {        throw no();    }

    @Override
    public void updateNCharacterStream(String arg0, Reader arg1) throws SQLException {        throw no();    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1) throws SQLException {        throw no();    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1) throws SQLException {        throw no();    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1) throws SQLException {        throw no();    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBlob(int arg0, InputStream arg1) throws SQLException {        throw no();    }

    @Override
    public void updateBlob(String arg0, InputStream arg1) throws SQLException {        throw no();    }

    @Override
    public void updateClob(int arg0, Reader arg1) throws SQLException {        throw no();    }

    @Override
    public void updateClob(String arg0, Reader arg1) throws SQLException {        throw no();    }

    @Override
    public void updateNClob(int arg0, Reader arg1) throws SQLException {        throw no();    }

    @Override
    public void updateNClob(String arg0, Reader arg1) throws SQLException {        throw no();    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {        throw no();    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {        throw no();    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException { throw no(); }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException { throw no(); }

    static UnsupportedOperationException no() {
        return new UnsupportedOperationException();
    }

}
