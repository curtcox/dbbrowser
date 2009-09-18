
package com.cve.db.dbio;

import com.cve.util.Check;
import com.cve.util.SimpleCache;
import com.google.common.collect.ImmutableList;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author curt
 */
final class DBMetaDataIOCache implements DBMetaDataIO {

    private final DBMetaDataIO io;

    private DBMetaDataIOCache(DBMetaDataIO io) {
        this.io = Check.notNull(io);
    }

    public static DBMetaDataIO of(DBMetaDataIO io) {
        return new DBMetaDataIOCache(io);
    }

    @Override
    public ImmutableList<TableInfo> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return io.getTables(catalog, schemaPattern, tableNamePattern, types);
    }

    private final Map<ColumnSpecifier,ImmutableList<ColumnInfo>> columns = SimpleCache.of();
    @Override
    public ImmutableList<ColumnInfo> getColumns(ColumnSpecifier specifier) throws SQLException {
        ImmutableList<ColumnInfo> info = columns.get(specifier);
        if (info!=null) {
            return info;
        }
        info = io.getColumns(specifier);
        columns.put(specifier, info);
        return info;
    }

    private final Map<KeySpecifier,ImmutableList<ReferencedKeyInfo>> importedKeys = SimpleCache.of();
    @Override
    public ImmutableList<ReferencedKeyInfo> getImportedKeys(KeySpecifier specifier) throws SQLException {
        ImmutableList<ReferencedKeyInfo> info = importedKeys.get(specifier);
        if (info!=null) {
            return info;
        }
        info = io.getImportedKeys(specifier);
        importedKeys.put(specifier, info);
        return info;
    }

    private final Map<KeySpecifier,ImmutableList<PrimaryKeyInfo>> primaryKeys = SimpleCache.of();
    @Override
    public ImmutableList<PrimaryKeyInfo> getPrimaryKeys(KeySpecifier specifier) throws SQLException {
        ImmutableList<PrimaryKeyInfo> info = primaryKeys.get(specifier);
        if (info!=null) {
            return info;
        }
        info = io.getPrimaryKeys(specifier);
        primaryKeys.put(specifier, info);
        return info;
    }

    private final Map<KeySpecifier,ImmutableList<ReferencedKeyInfo>> exportedKeys = SimpleCache.of();
    @Override
    public ImmutableList<ReferencedKeyInfo> getExportedKeys(KeySpecifier specifier) throws SQLException {
        ImmutableList<ReferencedKeyInfo> info = exportedKeys.get(specifier);
        if (info!=null) {
            return info;
        }
        info = io.getExportedKeys(specifier);
        exportedKeys.put(specifier, info);
        return info;
    }

    @Override
    public ImmutableList<CatalogInfo> getCatalogs() throws SQLException {
        return io.getCatalogs();
    }

    @Override
    public ImmutableList<SchemaInfo> getSchemas() throws SQLException {
        return io.getSchemas();
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return io.getMetaData();
    }

}
