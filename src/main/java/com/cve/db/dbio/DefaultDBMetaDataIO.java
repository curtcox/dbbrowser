
package com.cve.db.dbio;

import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.SimpleManagedFunction;
import com.cve.stores.UnpredictableFunction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import java.util.List;
import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * Low level access to database meta data.
 * TODO restore ResultSetRetry logic
 * @author curt
 */
final class DefaultDBMetaDataIO implements DBMetaDataIO {

    private final DefaultDBConnection connection;

    private static final String TABLE_NAME    = "TABLE_NAME";
    private static final String TABLE_SCHEM   = "TABLE_SCHEM";
    private static final String DATA_TYPE     = "DATA_TYPE";
    private static final String COLUMN_NAME   = "COLUMN_NAME";
    private static final String PKTABLE_CAT   = "PKTABLE_CAT";
    private static final String FKTABLE_CAT   = "FKTABLE_CAT";
    private static final String PKTABLE_NAME  = "PKTABLE_NAME";
    private static final String FKTABLE_NAME  = "FKTABLE_NAME";
    private static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
    private static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";

    private DefaultDBMetaDataIO(DefaultDBConnection connection) {
        this.connection = notNull(connection);
    }

    static DBMetaDataIO connection(DefaultDBConnection connection) {
        args(connection);
        return DBMetaDataIOCache.of(DBMetaDataIOTimer.of(new DefaultDBMetaDataIO(connection)));
    }

    private final ManagedFunction<TableSpecifier,DBResultSetIO> tables = SimpleManagedFunction.of(
        new UnpredictableFunction<TableSpecifier,DBResultSetIO>() {

        @Override
        public DBResultSetIO apply(TableSpecifier spec) throws Exception {
            return DBResultSetIO.of(getMetaData().getTables(spec.catalog, spec.schemaPattern, spec.tableNamePattern, spec.types));
        }
    });

    // Wrappers for all of the DBMD functions we use
    @Override
    public CurrentValue<ImmutableList<TableInfo>> getTables(TableSpecifier specifier) throws SQLException {
        DBResultSetIO results = tables.apply(specifier).value;
        List<TableInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String tableName = results.getString(r,TABLE_NAME);
            list.add(new TableInfo(tableName));
        }
        ImmutableList<TableInfo> tables = ImmutableList.copyOf(list);
        return CurrentValue.of(tables);
    }

    private final ManagedFunction<ColumnSpecifier,DBResultSetIO> columns = SimpleManagedFunction.of(
        new UnpredictableFunction<ColumnSpecifier,DBResultSetIO>() {

        @Override
        public DBResultSetIO apply(ColumnSpecifier specifier) throws Exception {
                return DBResultSetIO.of(
                        getMetaData().getColumns(specifier.catalog, specifier.schemaPattern, specifier.tableNamePattern,specifier.columnNamePattern));
        }
    });

    @Override
    public CurrentValue<ImmutableList<ColumnInfo>> getColumns(final ColumnSpecifier specifier) throws SQLException {
        DBResultSetIO results = columns.apply(specifier).value;
        List<ColumnInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String schemaName = results.getString(r,TABLE_SCHEM);
            String  tableName = results.getString(r,TABLE_NAME);
            String columnName = results.getString(r,COLUMN_NAME);
            int          type = results.getInt(r,DATA_TYPE);
            ColumnInfo column = new ColumnInfo(schemaName,tableName,columnName,type);
            list.add(column);
        }
        ImmutableList<ColumnInfo> infos = ImmutableList.copyOf(list);
        return CurrentValue.of(infos);
    }

    private final ManagedFunction<KeySpecifier,DBResultSetIO> importedKeys = SimpleManagedFunction.of(
        new UnpredictableFunction<KeySpecifier,DBResultSetIO>() {
        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getImportedKeys(specifier.catalog, specifier.schema, specifier.tableName));
        }
    });

    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getImportedKeys(final KeySpecifier specifier) throws SQLException {
        DBResultSetIO results = importedKeys.apply(specifier).value;
        List<ReferencedKeyInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String pkDatabase = results.getString(r,PKTABLE_CAT);
            String fkDatabase = results.getString(r,FKTABLE_CAT);
            String    pkTable = results.getString(r,PKTABLE_NAME);
            String    fkTable = results.getString(r,FKTABLE_NAME);
            String   pkColumn = results.getString(r,PKCOLUMN_NAME);
            String   fkColumn = results.getString(r,FKCOLUMN_NAME);
            list.add(new ReferencedKeyInfo(pkDatabase,fkDatabase,pkTable,fkTable,pkColumn,fkColumn));
        }
        ImmutableList<ReferencedKeyInfo> refs = ImmutableList.copyOf(list);
        return CurrentValue.of(refs);
    }

    private final ManagedFunction<KeySpecifier,DBResultSetIO> primaryKeys = SimpleManagedFunction.of(
        new UnpredictableFunction<KeySpecifier,DBResultSetIO>() {
        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getPrimaryKeys(specifier.catalog, specifier.schema, specifier.tableName));
        }
    });
    @Override
    public CurrentValue<ImmutableList<PrimaryKeyInfo>> getPrimaryKeys(final KeySpecifier specifier) throws SQLException {
        DBResultSetIO results = primaryKeys.apply(specifier).value;
        List<PrimaryKeyInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String columnName = results.getString(r,COLUMN_NAME);
            list.add(new PrimaryKeyInfo(columnName));
        }
        ImmutableList<PrimaryKeyInfo> keys = ImmutableList.copyOf(list);
        return CurrentValue.of(keys);
    }

    private final ManagedFunction<KeySpecifier,DBResultSetIO> exportedKeys = SimpleManagedFunction.of(
        new UnpredictableFunction<KeySpecifier,DBResultSetIO>() {
        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getExportedKeys(specifier.catalog, specifier.schema, specifier.tableName));
        }
    });

    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getExportedKeys(final KeySpecifier specifier) throws SQLException {
        DBResultSetIO results = exportedKeys.apply(specifier).value;
        List<ReferencedKeyInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String pkDatabase = results.getString(r,PKTABLE_CAT);
            String fkDatabase = results.getString(r,FKTABLE_CAT);
            String    pkTable = results.getString(r,PKTABLE_NAME);
            String    fkTable = results.getString(r,FKTABLE_NAME);
            String   pkColumn = results.getString(r,PKCOLUMN_NAME);
            String   fkColumn = results.getString(r,FKCOLUMN_NAME);
            list.add(new ReferencedKeyInfo(pkDatabase,fkDatabase,pkTable,fkTable,pkColumn,fkColumn));
        }
        ImmutableList<ReferencedKeyInfo> refs = ImmutableList.copyOf(list);
        return CurrentValue.of(refs);
    }

    private final ManagedFunction<KeySpecifier,DBResultSetIO> catalogs = SimpleManagedFunction.of(
        new UnpredictableFunction<KeySpecifier,DBResultSetIO>() {
        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getCatalogs());
        }
    });

    @Override
    public CurrentValue<ImmutableList<CatalogInfo>> getCatalogs() throws SQLException {
        DBResultSetIO results = catalogs.apply(null).value;
        List<CatalogInfo> list = Lists.newArrayList();
        // Due to a H2 driver bug, we can't use the column name
        int TABLE_CAT = 1;
        String databaseName = results.getString(0,TABLE_CAT);
        list.add(new CatalogInfo(databaseName));
        ImmutableList<CatalogInfo> infos = ImmutableList.copyOf(list);
        return CurrentValue.of(infos);
    }

    private final ManagedFunction<KeySpecifier,DBResultSetIO> schemas = SimpleManagedFunction.of(
        new UnpredictableFunction<KeySpecifier,DBResultSetIO>() {
        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
                return DBResultSetIO.of(getMetaData().getSchemas());
        }
    });

    @Override
    public CurrentValue<ImmutableList<SchemaInfo>> getSchemas() throws SQLException {
        DBResultSetIO results = schemas.apply(null).value;
        List<SchemaInfo> list = Lists.newArrayList();
        // Due to a H2 driver bug, we can't use the column name
        int SCHEMA_NAME = 1;
        String databaseName = results.getString(0,SCHEMA_NAME);
        list.add(new SchemaInfo(databaseName));
        ImmutableList<SchemaInfo> infos = ImmutableList.copyOf(list);
        return CurrentValue.of(infos);
    }

    /**
     * Return the raw meta data.  This is mostly for debugging.
     */
    @Override
    public DatabaseMetaData getMetaData() {
        return connection.getJDBCMetaData();
    }

}
