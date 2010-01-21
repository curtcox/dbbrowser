package com.cve.io.db;

import com.cve.log.Log;
import com.cve.stores.CurrentValue;
import static com.cve.util.Check.notNull;
import com.google.common.collect.ImmutableList;
import java.sql.DatabaseMetaData;

/**
 *
 * @author curt
 */
public final class DBMetaDataIOLogger implements DBMetaDataIO {

    private final DBMetaDataIO io;

    private DBMetaDataIOLogger(DBMetaDataIO io, Log log) {
        this.io = notNull(io);
        this.log = notNull(log);
    }

    public static DBMetaDataIO of(DBMetaDataIO io, Log log) {
        return new DBMetaDataIOLogger(io,log);
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
    final Log log;
    private void info(String mesage) { log.info(mesage);  }
    private void debug(String mesage) { log.debug(mesage);  }
}
