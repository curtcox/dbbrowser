
package com.cve.db.dbio;

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

    private final DBConnection connection;

    private DefaultDBMetaDataIO(DBConnection connection) {
        this.connection = notNull(connection);
    }

    public static DBMetaDataIO connection(DBConnection connection) {
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
                String tableName = results.getString("TABLE_NAME");
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
                String schemaName = results.getString("TABLE_SCHEM");
                String  tableName = results.getString("TABLE_NAME");
                String columnName = results.getString("COLUMN_NAME");
                int          type = results.getInt("DATA_TYPE");
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
                String pkDatabase = results.getString("PKTABLE_CAT");
                String fkDatabase = results.getString("FKTABLE_CAT");
                String    pkTable = results.getString("PKTABLE_NAME");
                String    fkTable = results.getString("FKTABLE_NAME");
                String   pkColumn = results.getString("PKCOLUMN_NAME");
                String   fkColumn = results.getString("FKCOLUMN_NAME");
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
                String columnName = results.getString("COLUMN_NAME");
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
                String pkDatabase = results.getString("PKTABLE_CAT");
                String fkDatabase = results.getString("FKTABLE_CAT");
                String    pkTable = results.getString("PKTABLE_NAME");
                String    fkTable = results.getString("FKTABLE_NAME");
                String   pkColumn = results.getString("PKCOLUMN_NAME");
                String   fkColumn = results.getString("FKCOLUMN_NAME");
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
                String databaseName = results.getString(SCHEMA_NAME);
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
}
