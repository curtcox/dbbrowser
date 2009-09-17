
package com.cve.db.dbio;

import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author curt
 */
final class CachedDBMetaDataIO implements DBMetaDataIO {

    private final DBMetaDataIO io;

    private CachedDBMetaDataIO(DBMetaDataIO io) {
        this.io = Check.notNull(io);
    }

    public static DBMetaDataIO of(DBMetaDataIO io) {
        return new CachedDBMetaDataIO(io);
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return io.getTables(catalog, schemaPattern, tableNamePattern, types);
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        return io.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
    }

    @Override
    public ImmutableList<ReferencedKeyInfo> getImportedKeys(KeySpecifier specifier) throws SQLException {
        return io.getImportedKeys(specifier);
    }

    @Override
    public ImmutableList<PrimaryKeyInfo> getPrimaryKeys(KeySpecifier specifier) throws SQLException {
        return io.getPrimaryKeys(specifier);
    }

    @Override
    public ImmutableList<ReferencedKeyInfo> getExportedKeys(KeySpecifier specifier) throws SQLException {
        return io.getExportedKeys(specifier);
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        return io.getCatalogs();
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        return io.getSchemas();
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return io.getMetaData();
    }

}
