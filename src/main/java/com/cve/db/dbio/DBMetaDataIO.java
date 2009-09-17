
package com.cve.db.dbio;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * Low level access to database meta data.
 * @author curt
 */
public final class DBMetaDataIO {

    private final DBConnection connection;

    private DBMetaDataIO(DBConnection connection) {
        this.connection = notNull(connection);
    }

    public static DBMetaDataIO connection(DBConnection connection) {
        args(connection);
        return new DBMetaDataIO(connection);
    }

    // Wrappers for all of the DBMD functions we use
    public ResultSet getTables(final String catalog, final String schemaPattern, final String tableNamePattern, final String[] types) throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types);
            }
        });
    }

    public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
               return getMetaData().getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            }
        });
    }

    public ResultSet getImportedKeys(final String catalog, final String schema, final String tableName) throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getImportedKeys(catalog, schema, tableName);
            }
        });
    }

    public ResultSet getPrimaryKeys(final String catalog, final String schema, final String tableName) throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getPrimaryKeys(catalog, schema, tableName);
            }
        });
    }

    public ResultSet getExportedKeys(final String catalog, final String schema, final String tableName) throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getExportedKeys(catalog, schema, tableName);
            }
        });
    }

    public ResultSet getCatalogs() throws SQLException {
        return ResultSetRetry.run(connection,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                return getMetaData().getCatalogs();
            }
        });
    }

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
    public DatabaseMetaData getMetaData() {
        return connection.getJDBCMetaData();
    }



}
