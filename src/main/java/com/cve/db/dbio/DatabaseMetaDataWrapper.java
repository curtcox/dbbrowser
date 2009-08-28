package com.cve.db.dbio;

import com.cve.util.Check;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Wraps one meta data to provide another.
 * This shows what methods we actually use, and provides points to
 * intercept and debug.
 * @author curt
 */
final class DatabaseMetaDataWrapper extends NoDatabaseMetaData {

    private final DatabaseMetaData meta;

    private DatabaseMetaDataWrapper(DatabaseMetaData meta) {
        this.meta = Check.notNull(meta);
    }

    public static DatabaseMetaData of(DatabaseMetaData meta) {
        return new DatabaseMetaDataWrapper(meta);
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        return meta.getCatalogs();
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        return meta.getSchemas();
    }

    @Override
    public ResultSet getTables(String arg0, String arg1, String arg2, String[] arg3) throws SQLException {
        return meta.getTables(arg0, arg1, arg2, arg3);
    }

    @Override
    public ResultSet getColumns(String arg0, String arg1, String arg2, String arg3) throws SQLException {
        return meta.getColumns(arg0, arg1, arg2, arg3);
    }

    @Override
    public ResultSet getPrimaryKeys(String arg0, String arg1, String arg2) throws SQLException {
        return meta.getPrimaryKeys(arg0, arg1, arg2);
    }

    @Override
    public ResultSet getImportedKeys(String arg0, String arg1, String arg2) throws SQLException {
        return meta.getImportedKeys(arg0, arg1, arg2);
    }

    @Override
    public ResultSet getExportedKeys(String arg0, String arg1, String arg2) throws SQLException {
        return meta.getExportedKeys(arg0, arg1, arg2);
    }

    @Override
    public ResultSet getCrossReference(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws SQLException {
        return meta.getCrossReference(arg0, arg1, arg2, arg3, arg4, arg5);
    }

}
