package com.cve.db.dbio;

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
    public CurrentValue<ImmutableList<TableInfo>> getTables(TableSpecifier specifier) throws SQLException {
        Stopwatch watch = Stopwatch.start();
        CurrentValue<ImmutableList<TableInfo>> result = io.getTables(specifier);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<ColumnInfo> getColumns(ColumnSpecifier specifier) throws SQLException {
        Stopwatch watch = Stopwatch.start(specifier);
        ImmutableList<ColumnInfo> result = io.getColumns(specifier);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<ReferencedKeyInfo> getImportedKeys(KeySpecifier specifier) throws SQLException {
        Stopwatch watch = Stopwatch.start(specifier);
        ImmutableList<ReferencedKeyInfo> result = io.getImportedKeys(specifier);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<PrimaryKeyInfo> getPrimaryKeys(KeySpecifier specifier) throws SQLException {
        Stopwatch watch = Stopwatch.start(specifier);
        ImmutableList<PrimaryKeyInfo> result = io.getPrimaryKeys(specifier);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<ReferencedKeyInfo> getExportedKeys(KeySpecifier specifier) throws SQLException {
        Stopwatch watch = Stopwatch.start(specifier);
        ImmutableList<ReferencedKeyInfo> result = io.getExportedKeys(specifier);
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<CatalogInfo> getCatalogs() throws SQLException {
        Stopwatch watch = Stopwatch.start();
        ImmutableList<CatalogInfo> result = io.getCatalogs();
        watch.stop();
        return result;
    }

    @Override
    public ImmutableList<SchemaInfo> getSchemas() throws SQLException {
        Stopwatch watch = Stopwatch.start();
        ImmutableList<SchemaInfo> result = io.getSchemas();
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
