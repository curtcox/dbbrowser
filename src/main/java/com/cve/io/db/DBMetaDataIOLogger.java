package com.cve.io.db;

import com.cve.log.Log;
import com.cve.stores.CurrentValue;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.sql.DatabaseMetaData;

/**
 *
 * @author curt
 */
public final class DBMetaDataIOLogger implements DBMetaDataIO {

    private final DBMetaDataIO io;

    private DBMetaDataIOLogger(DBMetaDataIO io) {
        this.io = Check.notNull(io);
    }

    public static DBMetaDataIO of(DBMetaDataIO io) {
        return new DBMetaDataIOLogger(io);
    }

    @Override
    public CurrentValue<ImmutableList<TableInfo>> getTables(TableSpecifier specifier) {
        return log(io.getTables(specifier));
    }

    @Override
    public CurrentValue<ImmutableList<ColumnInfo>> getColumns(ColumnSpecifier specifier) {
        return log(io.getColumns(specifier));
    }

    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getImportedKeys(KeySpecifier specifier) {
        return log(io.getImportedKeys(specifier));
    }

    @Override
    public CurrentValue<ImmutableList<PrimaryKeyInfo>> getPrimaryKeys(KeySpecifier specifier) {
        return log(io.getPrimaryKeys(specifier));
    }

    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getExportedKeys(KeySpecifier specifier) {
        return log( io.getExportedKeys(specifier));
    }

    @Override
    public CurrentValue<ImmutableList<CatalogInfo>> getCatalogs() {
        return log( io.getCatalogs());
    }

    @Override
    public CurrentValue<ImmutableList<SchemaInfo>> getSchemas() {
        return log( io.getSchemas());
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return log( io.getMetaData());
    }

    <T> T log(T t) {
        debug("" + t);
        return t;
    }

    /**
     * Logging stuff.
     */
    static final Log LOG = Log.of(DBMetaDataIOLogger.class);
    private static void info(String mesage) { LOG.info(mesage);  }
    private static void debug(String mesage) { LOG.debug(mesage);  }
}
