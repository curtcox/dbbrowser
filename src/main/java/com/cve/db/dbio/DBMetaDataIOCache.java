
package com.cve.db.dbio;

import com.cve.stores.CurrentValue;
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
    public CurrentValue<ImmutableList<TableInfo>> getTables(TableSpecifier specifier) throws SQLException {
        return io.getTables(specifier);
    }

    private final Map<ColumnSpecifier,CurrentValue<ImmutableList<ColumnInfo>>> columns = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<ColumnInfo>> getColumns(ColumnSpecifier specifier) throws SQLException {
        CurrentValue<ImmutableList<ColumnInfo>> info = columns.get(specifier);
        if (info!=null) {
            return info;
        }
        info = io.getColumns(specifier);
        columns.put(specifier, info);
        return info;
    }

    private final Map<KeySpecifier,CurrentValue<ImmutableList<ReferencedKeyInfo>>> importedKeys = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getImportedKeys(KeySpecifier specifier) throws SQLException {
        CurrentValue<ImmutableList<ReferencedKeyInfo>> info = importedKeys.get(specifier);
        if (info!=null) {
            return info;
        }
        info = io.getImportedKeys(specifier);
        importedKeys.put(specifier, info);
        return info;
    }

    private final Map<KeySpecifier,CurrentValue<ImmutableList<PrimaryKeyInfo>>> primaryKeys = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<PrimaryKeyInfo>> getPrimaryKeys(KeySpecifier specifier) throws SQLException {
        CurrentValue<ImmutableList<PrimaryKeyInfo>> info = primaryKeys.get(specifier);
        if (info!=null) {
            return info;
        }
        info = io.getPrimaryKeys(specifier);
        primaryKeys.put(specifier, info);
        return info;
    }

    private final Map<KeySpecifier,CurrentValue<ImmutableList<ReferencedKeyInfo>>> exportedKeys = SimpleCache.of();
    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getExportedKeys(KeySpecifier specifier) throws SQLException {
        CurrentValue<ImmutableList<ReferencedKeyInfo>> info = exportedKeys.get(specifier);
        if (info!=null) {
            return info;
        }
        info = io.getExportedKeys(specifier);
        exportedKeys.put(specifier, info);
        return info;
    }

    @Override
    public CurrentValue<ImmutableList<CatalogInfo>> getCatalogs() throws SQLException {
        return io.getCatalogs();
    }

    @Override
    public CurrentValue<ImmutableList<SchemaInfo>> getSchemas() throws SQLException {
        return io.getSchemas();
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return io.getMetaData();
    }

}
