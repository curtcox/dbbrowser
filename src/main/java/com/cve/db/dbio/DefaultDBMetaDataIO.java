
package com.cve.db.dbio;

import com.cve.util.Canonicalizer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * Low level access to database meta data.
 * TODO restore ResultSetRetry logic
 * @author curt
 */
public final class DefaultDBMetaDataIO implements DBMetaDataIO {

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

    public static DBMetaDataIO connection(DefaultDBConnection connection) {
        args(connection);
        return DBMetaDataIOCache.of(DBMetaDataIOTimer.of(new DefaultDBMetaDataIO(connection)));
    }

    // Wrappers for all of the DBMD functions we use
    @Override
    public ImmutableList<TableInfo> getTables(final String catalog, final String schemaPattern, final String tableNamePattern, final String[] types) throws SQLException {
        ResultSetGenerator generator = new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types);
            }
        };
        ResultSet results = ResultSetRetry.run(connection, generator);
        try {
            List<TableInfo> list = Lists.newArrayList();
            while (results.next()) {
                String tableName = getString(results,TABLE_NAME);
                list.add(new TableInfo(tableName));
            }
            ImmutableList<TableInfo> tables = ImmutableList.copyOf(list);
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<ColumnInfo> getColumns(final ColumnSpecifier specifier) throws SQLException {
        ResultSetGenerator generator = new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getColumns(specifier.catalog, specifier.schemaPattern, specifier.tableNamePattern,specifier.columnNamePattern);
            }
        };
        ResultSet results = ResultSetRetry.run(connection, generator);
        try {
            List<ColumnInfo> list = Lists.newArrayList();
            while (results.next()) {
                String schemaName = getString(results,TABLE_SCHEM);
                String  tableName = getString(results,TABLE_NAME);
                String columnName = getString(results,COLUMN_NAME);
                int          type = results.getInt(DATA_TYPE);
                ColumnInfo column = new ColumnInfo(schemaName,tableName,columnName,type);
                list.add(column);
            }
            ImmutableList<ColumnInfo> infos = ImmutableList.copyOf(list);
            return infos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<ReferencedKeyInfo> getImportedKeys(final KeySpecifier specifier) throws SQLException {
        ResultSetGenerator generator = new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getImportedKeys(specifier.catalog, specifier.schema, specifier.tableName);
            }
        };
        ResultSet results = ResultSetRetry.run(connection, generator);
        try {
            List<ReferencedKeyInfo> list = Lists.newArrayList();
            while (results.next()) {
                String pkDatabase = getString(results,PKTABLE_CAT);
                String fkDatabase = getString(results,FKTABLE_CAT);
                String    pkTable = getString(results,PKTABLE_NAME);
                String    fkTable = getString(results,FKTABLE_NAME);
                String   pkColumn = getString(results,PKCOLUMN_NAME);
                String   fkColumn = getString(results,FKCOLUMN_NAME);
                list.add(new ReferencedKeyInfo(pkDatabase,fkDatabase,pkTable,fkTable,pkColumn,fkColumn));
            }
            ImmutableList<ReferencedKeyInfo> refs = ImmutableList.copyOf(list);
            return refs;
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<PrimaryKeyInfo> getPrimaryKeys(final KeySpecifier specifier) throws SQLException {
        ResultSetGenerator generator = new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getPrimaryKeys(specifier.catalog, specifier.schema, specifier.tableName);
            }
        };
        ResultSet results = ResultSetRetry.run(connection, generator);
        try {
            List<PrimaryKeyInfo> list = Lists.newArrayList();
            while (results.next()) {
                String columnName = getString(results,COLUMN_NAME);
                list.add(new PrimaryKeyInfo(columnName));
            }
            ImmutableList<PrimaryKeyInfo> keys = ImmutableList.copyOf(list);
            return keys;
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<ReferencedKeyInfo> getExportedKeys(final KeySpecifier specifier) throws SQLException {
        ResultSetGenerator generator = new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getExportedKeys(specifier.catalog, specifier.schema, specifier.tableName);
            }
        };
        ResultSet results = ResultSetRetry.run(connection, generator);
        try {
            List<ReferencedKeyInfo> list = Lists.newArrayList();
            while (results.next()) {
                String pkDatabase = getString(results,PKTABLE_CAT);
                String fkDatabase = getString(results,FKTABLE_CAT);
                String    pkTable = getString(results,PKTABLE_NAME);
                String    fkTable = getString(results,FKTABLE_NAME);
                String   pkColumn = getString(results,PKCOLUMN_NAME);
                String   fkColumn = getString(results,FKCOLUMN_NAME);
                list.add(new ReferencedKeyInfo(pkDatabase,fkDatabase,pkTable,fkTable,pkColumn,fkColumn));
            }
            ImmutableList<ReferencedKeyInfo> refs = ImmutableList.copyOf(list);
            return refs;
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<CatalogInfo> getCatalogs() throws SQLException {
        ResultSetGenerator generator = new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getCatalogs();
            }
        };
        ResultSet results = ResultSetRetry.run(connection, generator);
        try {
            List<CatalogInfo> list = Lists.newArrayList();
            while (results.next()) {
                // Due to a H2 driver bug, we can't use the column name
                int TABLE_CAT = 1;
                String databaseName = results.getString(TABLE_CAT);
                list.add(new CatalogInfo(databaseName));
            }
            ImmutableList<CatalogInfo> infos = ImmutableList.copyOf(list);
            return infos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<SchemaInfo> getSchemas() throws SQLException {
        ResultSetGenerator generator = new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getSchemas();
            }
        };
        ResultSet results = ResultSetRetry.run(connection, generator);
        try {
            List<SchemaInfo> list = Lists.newArrayList();
            while (results.next()) {
                // Due to a H2 driver bug, we can't use the column name
                int SCHEMA_NAME = 1;
                String databaseName = getString(results,SCHEMA_NAME);
                list.add(new SchemaInfo(databaseName));
            }
            ImmutableList<SchemaInfo> infos = ImmutableList.copyOf(list);
            return infos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    /**
     * Return the raw meta data.  This is mostly for debugging.
     */
    @Override
    public DatabaseMetaData getMetaData() {
        return connection.getJDBCMetaData();
    }

    /**
     * Handy static method to close a result set without needing a try/catch
     */
    public static void close(ResultSet results) {
        try {
            results.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Canonicalizer<String> canonicalizer = Canonicalizer.of();
    private String getString(ResultSet results, String key) throws SQLException {
        return canonicalizer.canonical(results.getString(key));
    }

    private String getString(ResultSet results, int pos) throws SQLException {
        return canonicalizer.canonical(results.getString(pos));
    }

}
