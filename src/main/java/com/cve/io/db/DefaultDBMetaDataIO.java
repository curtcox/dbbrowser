
package com.cve.io.db;

import com.cve.log.Log;
import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnpredictableFunction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import java.util.List;
import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * Low level access to database meta data.
 * TODO restore ResultSetRetry logic
 * @author curt
 */
public class DefaultDBMetaDataIO implements DBMetaDataIO {

    /**
     * How we connect to databases.
     */
    private final DBConnection connection;

    private final ManagedFunction<TableSpecifier,DBResultSetIO> tables;
    private final ManagedFunction<ColumnSpecifier,DBResultSetIO> columns;
    private final ManagedFunction<KeySpecifier,DBResultSetIO> importedKeys;
    private final ManagedFunction<KeySpecifier,DBResultSetIO> primaryKeys;
    private final ManagedFunction<KeySpecifier,DBResultSetIO> exportedKeys;
    private final ManagedFunction<KeySpecifier,DBResultSetIO> catalogs;
    private final ManagedFunction<KeySpecifier,DBResultSetIO> schemas;

    public static final String TABLE_NAME    = "TABLE_NAME";
    public static final String TABLE_SCHEM   = "TABLE_SCHEM";
    public static final String DATA_TYPE     = "DATA_TYPE";
    public static final String COLUMN_NAME   = "COLUMN_NAME";
    public static final String PKTABLE_CAT   = "PKTABLE_CAT";
    public static final String FKTABLE_CAT   = "FKTABLE_CAT";
    public static final String PKTABLE_NAME  = "PKTABLE_NAME";
    public static final String FKTABLE_NAME  = "FKTABLE_NAME";
    public static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
    public static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";

    protected DefaultDBMetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction) {
        this.connection = notNull(connection);
        notNull(managedFunction);
              tables = notNull(managedFunction.of(new GetTables(),       TableSpecifier.class,  DBResultSetIO.class, DBResultSetIO.NULL));
             columns = notNull(managedFunction.of(new GetColumns(),      ColumnSpecifier.class, DBResultSetIO.class, DBResultSetIO.NULL));
        importedKeys = notNull(managedFunction.of(new GetImportedKeys(), KeySpecifier.class,    DBResultSetIO.class, DBResultSetIO.NULL));
         primaryKeys = notNull(managedFunction.of(new GetPrimaryKeys(),  KeySpecifier.class,    DBResultSetIO.class, DBResultSetIO.NULL));
        exportedKeys = notNull(managedFunction.of(new GetExportedKeys(), KeySpecifier.class,    DBResultSetIO.class, DBResultSetIO.NULL));
            catalogs = notNull(managedFunction.of(new GetCatalogs(),     Void.class,            DBResultSetIO.class, DBResultSetIO.NULL));
             schemas = notNull(managedFunction.of(new GetSchemas(),      Void.class,            DBResultSetIO.class, DBResultSetIO.NULL));
    }

    // Wrappers for all of the DBMD functions we use
    @Override
    public CurrentValue<ImmutableList<TableInfo>> getTables(TableSpecifier specifier) {
        DBResultSetIO results = tables.apply(specifier).value;
        List<TableInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String tableName = results.getString(r,TABLE_NAME);
            list.add(new TableInfo(tableName));
        }
        ImmutableList<TableInfo> tableList = ImmutableList.copyOf(list);
        return CurrentValue.of(tableList);
    }


    /**
     * See
     * http://java.sun.com/javase/6/docs/api/java/sql/DatabaseMetaData.html#getColumns%28java.lang.String,%20java.lang.String,%20java.lang.String,%20java.lang.String%29
     * @param specifier
     * @return
     */
    @Override
    public CurrentValue<ImmutableList<ColumnInfo>> getColumns(final ColumnSpecifier specifier) {
        DBResultSetIO results = columns.apply(specifier).value;
        List<ColumnInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String schemaName = results.getString(r,TABLE_SCHEM);
            String  tableName = results.getString(r,TABLE_NAME);
            String columnName = results.getString(r,COLUMN_NAME);
            int          type = (int) results.getLong(r,DATA_TYPE);
            ColumnInfo column = ColumnInfo.of(schemaName,tableName,columnName,type);
            list.add(column);
        }
        ImmutableList<ColumnInfo> infos = ImmutableList.copyOf(list);
        return CurrentValue.of(infos);
    }


    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getImportedKeys(final KeySpecifier specifier)  {
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

    @Override
    public CurrentValue<ImmutableList<PrimaryKeyInfo>> getPrimaryKeys(final KeySpecifier specifier) {
        DBResultSetIO results = primaryKeys.apply(specifier).value;
        List<PrimaryKeyInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String columnName = results.getString(r,COLUMN_NAME);
            list.add(new PrimaryKeyInfo(columnName));
        }
        ImmutableList<PrimaryKeyInfo> keys = ImmutableList.copyOf(list);
        return CurrentValue.of(keys);
    }


    @Override
    public CurrentValue<ImmutableList<ReferencedKeyInfo>> getExportedKeys(final KeySpecifier specifier) {
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


    @Override
    public CurrentValue<ImmutableList<CatalogInfo>> getCatalogs() {
        DBResultSetIO results = catalogs.apply(null).value;
        List<CatalogInfo> list = Lists.newArrayList();
        // Due to a H2 driver bug, we can't use the column name
        int TABLE_CAT = 1;
        String databaseName = results.getString(0,TABLE_CAT);
        list.add(new CatalogInfo(databaseName));
        ImmutableList<CatalogInfo> infos = ImmutableList.copyOf(list);
        return CurrentValue.of(infos);
    }

    @Override
    public CurrentValue<ImmutableList<SchemaInfo>> getSchemas() {
        CurrentValue<DBResultSetIO> current = schemas.apply(null);
        DBResultSetIO results = current.value;
        if (results.rows.size()<1) {
            ImmutableList<SchemaInfo> empty = ImmutableList.of();
            return CurrentValue.of(empty);
        }
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
        return ((DefaultDBConnection) connection).getJDBCMetaData();
    }

    private final class GetTables implements UnpredictableFunction<TableSpecifier, DBResultSetIO> {
        @Override
        public DBResultSetIO apply(TableSpecifier spec) throws Exception {
            ResultSet results = getMetaData().getTables(spec.catalog, spec.schemaPattern, spec.tableNamePattern, spec.types);
            // return DBResultSetIO.of(results,Getter.string(TABLE_NAME));
            return DBResultSetIO.of(results);
        }
    }

    private final class GetColumns implements UnpredictableFunction<ColumnSpecifier, DBResultSetIO> {

        @Override
        public DBResultSetIO apply(ColumnSpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getColumns(specifier.catalog, specifier.schemaPattern, specifier.tableNamePattern, specifier.columnNamePattern));
        }
    }

    private final class GetImportedKeys implements UnpredictableFunction<KeySpecifier, DBResultSetIO> {

        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getImportedKeys(specifier.catalog, specifier.schema, specifier.tableName));
        }
    }

    private final class GetPrimaryKeys implements UnpredictableFunction<KeySpecifier, DBResultSetIO> {

        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getPrimaryKeys(specifier.catalog, specifier.schema, specifier.tableName));
        }
    }

    private final class GetExportedKeys implements UnpredictableFunction<KeySpecifier, DBResultSetIO> {

        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getExportedKeys(specifier.catalog, specifier.schema, specifier.tableName));
        }
    }

    private final class GetCatalogs implements UnpredictableFunction<KeySpecifier, DBResultSetIO> {

        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getCatalogs());
        }
    }

    private final class GetSchemas implements UnpredictableFunction<KeySpecifier, DBResultSetIO> {

        @Override
        public DBResultSetIO apply(KeySpecifier specifier) throws Exception {
            return DBResultSetIO.of(getMetaData().getSchemas());
        }
    }

    /**
     * Logging stuff.
     */
    static final Log LOG = Log.of(DefaultDBMetaDataIO.class);
    private static void info(String mesage) { LOG.info(mesage);  }
    private static void debug(String mesage) { LOG.debug(mesage);  }
}
