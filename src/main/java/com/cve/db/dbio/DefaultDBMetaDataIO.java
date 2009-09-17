
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
 * @author curt
 */
public final class DefaultDBMetaDataIO implements DBMetaDataIO {

    private final DBConnection connection;

    private DefaultDBMetaDataIO(DBConnection connection) {
        this.connection = notNull(connection);
    }

    public static DBMetaDataIO connection(DBConnection connection) {
        args(connection);
        return CachedDBMetaDataIO.of(new DefaultDBMetaDataIO(connection));
    }

    // Wrappers for all of the DBMD functions we use
    @Override
    public ResultSet getTables(final String catalog, final String schemaPattern, final String tableNamePattern, final String[] types) throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types);
            }
        });
    }

    @Override
    public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
               return getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            }
        });
    }

    @Override
    public ImmutableList<ReferencedKeyInfo> getImportedKeys(final KeySpecifier specifier) throws SQLException {
        ResultSet results = getMetaData().getImportedKeys(specifier.catalog, specifier.schema, specifier.tableName);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<PrimaryKeyInfo> getPrimaryKeys(final KeySpecifier specifier) throws SQLException {
        ResultSet results = getMetaData().getPrimaryKeys(specifier.catalog, specifier.schema, specifier.tableName);
        try {
            List<PrimaryKeyInfo> list = Lists.newArrayList();
            while (results.next()) {
                String columnName = results.getString("COLUMN_NAME");
                list.add(new PrimaryKeyInfo(columnName));
            }
            ImmutableList<PrimaryKeyInfo> keys = ImmutableList.copyOf(list);
            return keys;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<ReferencedKeyInfo> getExportedKeys(final KeySpecifier specifier) throws SQLException {
        ResultSet results = getMetaData().getExportedKeys(specifier.catalog, specifier.schema, specifier.tableName);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getCatalogs();
            }
        });
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getSchemas();
            }
        });
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
