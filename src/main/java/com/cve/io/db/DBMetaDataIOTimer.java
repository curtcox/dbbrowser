package com.cve.io.db;

import com.cve.stores.CurrentValue;
import com.cve.util.Check;
import com.cve.util.Stopwatch;
import com.google.common.collect.ImmutableList;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 *
 * @author curt
 */
final class DBMetaDataIOTimer implements DBMetaDataIO {

    private final DBMetaDataIO io;

    private DBMetaDataIOTimer(DBMetaDataIO io) {
        this.io = Check.notNull(io);
    }

    static DBMetaDataIO of(DBMetaDataIO io) {
        return new DBMetaDataIOTimer(io);
    }

    @Override
    public CurrentValue<ImmutableList<TableInfo>> getTables(TableSpecifier specifier) {
        Stopwatch watch = Stopwatch.start();
        CurrentValue<ImmutableList<TableInfo>> result = io.getTables(specifier);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<ColumnInfo>> getColumns(ColumnSpecifier specifier) {
        Stopwatch watch = Stopwatch.start(specifier);
        CurrentValue<ImmutableList<ColumnInfo>> result = io.getColumns(specifier);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getImportedKeys(KeySpecifier specifier) {
        Stopwatch watch = Stopwatch.start(specifier);
        CurrentValue<ImmutableList<ReferencedKeyInfo>> result = io.getImportedKeys(specifier);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<PrimaryKeyInfo>> getPrimaryKeys(KeySpecifier specifier) {
        Stopwatch watch = Stopwatch.start(specifier);
        CurrentValue<ImmutableList<PrimaryKeyInfo>> result = io.getPrimaryKeys(specifier);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getExportedKeys(KeySpecifier specifier) {
        Stopwatch watch = Stopwatch.start(specifier);
        CurrentValue<ImmutableList<ReferencedKeyInfo>> result = io.getExportedKeys(specifier);
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<CatalogInfo>> getCatalogs() {
        Stopwatch watch = Stopwatch.start();
        CurrentValue<ImmutableList<CatalogInfo>> result = io.getCatalogs();
        watch.stop();
        return result;
    }

    @Override
    public CurrentValue<ImmutableList<SchemaInfo>> getSchemas() {
        Stopwatch watch = Stopwatch.start();
        CurrentValue<ImmutableList<SchemaInfo>> result = io.getSchemas();
        watch.stop();
        return result;
    }

    @Override
    public DatabaseMetaData getMetaData() {
        Stopwatch watch = Stopwatch.start();
        DatabaseMetaData result = io.getMetaData();
        watch.stop();
        return result;
    }

}
