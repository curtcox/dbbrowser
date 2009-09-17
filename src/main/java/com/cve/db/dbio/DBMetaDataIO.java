
package com.cve.db.dbio;

import com.cve.util.Strings;
import com.google.common.collect.ImmutableList;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.concurrent.Immutable;


/**
 * Low level access to database meta data.
 * @author curt
 */
public interface DBMetaDataIO {

    @Immutable
    public static class KeySpecifier {

        // any of these might need to be null
        public final String catalog;
        public final String schema;
        public final String tableName;

        private KeySpecifier(String catalog,String schema,String tableName) {
            this.catalog = catalog;
            this.schema = schema;
            this.tableName = tableName;
        }

        public static KeySpecifier of(String catalog,String schema,String tableName) {
            return new KeySpecifier(catalog,schema,tableName);
        }

        @Override
        public int hashCode() {
            return Strings.hashCode(catalog) ^ Strings.hashCode(schema) ^ Strings.hashCode(tableName);
        }

        @Override
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        public boolean equals(Object o) {
            KeySpecifier other = (KeySpecifier) o;
            return Strings.equals(catalog,other.catalog) && Strings.equals(schema,other.schema) && Strings.equals(tableName,other.tableName);

        }
    }

    public static class PrimaryKeyInfo {
        public final String columnName;
        PrimaryKeyInfo(String columnName) {
            this.columnName = columnName;
        }
    }

    public static class ReferencedKeyInfo {
        public final String pkDatabase;
        public final String fkDatabase;
        public final String pkTable;
        public final String fkTable;
        public final String pkColumn;
        public final String fkColumn;

        ReferencedKeyInfo(String pkDatabase,String fkDatabase,String pkTable,String fkTable,String pkColumn,String fkColumn) {
            this.pkDatabase = pkDatabase;
            this.fkDatabase = fkDatabase;
            this.pkTable    = pkTable;
            this.fkTable    = fkTable;
            this.pkColumn   = pkColumn;
            this.fkColumn   = fkColumn;
        }
    }

    // Wrappers for all of the DBMD functions we use
    ResultSet getTables(final String catalog, final String schemaPattern, final String tableNamePattern, final String[] types) throws SQLException;

    ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException;

    ImmutableList<ReferencedKeyInfo> getImportedKeys(KeySpecifier specifier) throws SQLException;

    ImmutableList<PrimaryKeyInfo> getPrimaryKeys(KeySpecifier specifier) throws SQLException;

    ImmutableList<ReferencedKeyInfo> getExportedKeys(KeySpecifier specifier) throws SQLException;

    ResultSet getCatalogs() throws SQLException;

    ResultSet getSchemas() throws SQLException;

    /**
     * Return the raw meta data.  This is mostly for debugging.
     */
    DatabaseMetaData getMetaData();



}
