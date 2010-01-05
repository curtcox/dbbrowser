
package com.cve.db.dbio;

import com.cve.stores.CurrentValue;
import com.cve.util.Strings;
import com.google.common.collect.ImmutableList;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.annotation.concurrent.Immutable;


/**
 * Low level access to database meta data.
 * This interface is used by our database drivers to access the database.
 * @author curt
 */
public interface DBMetaDataIO {

    @Immutable
    public static final class KeySpecifier {

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

        @Override
        public String toString() {
            return " catalog=" + catalog +
                   " schema=" + schema +
                   " tableName=" + tableName;
        }
    }

    @Immutable
    public static final class ColumnSpecifier {

        // any of these might need to be null
        public final String catalog;
        public final String schemaPattern;
        public final String tableNamePattern;
        public final String columnNamePattern;

        private ColumnSpecifier(String catalog,String schemaPattern,String tableNamePattern,String columnNamePattern) {
            this.catalog = catalog;
            this.schemaPattern = schemaPattern;
            this.tableNamePattern = tableNamePattern;
            this.columnNamePattern = columnNamePattern;
        }

        public static ColumnSpecifier of(String catalog,String schemaPattern,String tableNamePattern,String columnNamePattern) {
            return new ColumnSpecifier(catalog,schemaPattern,tableNamePattern,columnNamePattern);
        }

        @Override
        public int hashCode() {
            return Strings.hashCode(catalog) ^
                   Strings.hashCode(schemaPattern) ^
                   Strings.hashCode(tableNamePattern) ^
                   Strings.hashCode(columnNamePattern);
        }

        @Override
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        public boolean equals(Object o) {
            ColumnSpecifier other = (ColumnSpecifier) o;
            return Strings.equals(catalog,other.catalog) &&
                   Strings.equals(schemaPattern,other.schemaPattern) &&
                   Strings.equals(tableNamePattern,other.tableNamePattern) &&
                   Strings.equals(columnNamePattern,other.columnNamePattern)
            ;
        }

        @Override
        public String toString() {
            return " catalog=" + catalog +
                   " schemaPattern=" + schemaPattern +
                   " tableNamePattern=" + tableNamePattern +
                   " columnNamePattern=" + columnNamePattern
            ;
        }
    }

    @Immutable
    public static final class PrimaryKeyInfo {
        public final String columnName;
        PrimaryKeyInfo(String columnName) {
            this.columnName = columnName;
        }
    }

    @Immutable
    public static final class TableInfo {
        public final String tableName;
        TableInfo(String tableName) {
            this.tableName = tableName;
        }
    }

    @Immutable
    public static final class ColumnInfo {
        public final String tableSchema;
        public final String tableName;
        public final String columnName;
        public final int dataType;
        // Due to a H2 driver bug, we can't use the column name
        static int TABLE_SCHEMA = 2;
    ColumnInfo(String tableSchema, String tableName, String columnName, int dataType) {
            this.tableSchema = tableSchema;
            this.tableName   = tableName;
            this.columnName  = columnName;
            this.dataType    = dataType;
        }
    }

    @Immutable
    public static final class CatalogInfo {
        public final String databaseName;
        CatalogInfo(String databaseName) {
            this.databaseName = databaseName;
        }
    }

    @Immutable
    public static final class SchemaInfo {
        public final String schemaName;

        SchemaInfo(String schemaName) {
            this.schemaName = schemaName;
        }
    }

    @Immutable
    public static final class ReferencedKeyInfo {
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

    @Immutable
    public static final class TableSpecifier {
        final String catalog;
        final String schemaPattern;
        final String tableNamePattern;
        final String[] types;
        private TableSpecifier(String catalog, String schemaPattern, String tableNamePattern, String[] types) {
            this.catalog = catalog;
            this.schemaPattern = schemaPattern;
            this.tableNamePattern = tableNamePattern;
            this.types = types;
        }
        public static TableSpecifier of(String catalog, String schemaPattern, String tableNamePattern, String[] types) {
            return new TableSpecifier(catalog,schemaPattern,tableNamePattern,types);
        }
    }

    // Wrappers for all of the DBMD functions we use
    CurrentValue<ImmutableList<TableInfo>> getTables(TableSpecifier specifier);

    CurrentValue<ImmutableList<ColumnInfo>> getColumns(ColumnSpecifier specifier);

    CurrentValue<ImmutableList<ReferencedKeyInfo>> getImportedKeys(KeySpecifier specifier);

    CurrentValue<ImmutableList<PrimaryKeyInfo>> getPrimaryKeys(KeySpecifier specifier);

    CurrentValue<ImmutableList<ReferencedKeyInfo>> getExportedKeys(KeySpecifier specifier);

    CurrentValue<ImmutableList<CatalogInfo>> getCatalogs();

    CurrentValue<ImmutableList<SchemaInfo>> getSchemas();

    /**
     * Return the raw meta data.  This is mostly for debugging.
     */
    DatabaseMetaData getMetaData();



}
