package com.cve.io.db.driver;

import com.cve.io.db.DBResultSetIO;
import com.cve.io.db.DBMetaDataLogger;
import com.cve.io.db.DBMetaDataTimer;
import com.cve.io.db.DBMetaDataCache;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataLocked;
import com.cve.io.db.DBConnectionFactory;
import com.cve.io.db.DBMetaDataIO;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBColumn.Keyness;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.Join;
import com.cve.model.db.SQL;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBMetaDataIO.CatalogInfo;
import com.cve.io.db.DBMetaDataIO.ColumnInfo;
import com.cve.io.db.DBMetaDataIO.ColumnSpecifier;
import com.cve.io.db.DBMetaDataIO.KeySpecifier;
import com.cve.io.db.DBMetaDataIO.PrimaryKeyInfo;
import com.cve.io.db.DBMetaDataIO.ReferencedKeyInfo;
import com.cve.io.db.DBMetaDataIO.TableInfo;
import com.cve.io.db.DBMetaDataIO.TableSpecifier;
import com.cve.io.db.DefaultDBConnection;
import com.cve.io.db.DefaultDBMetaDataIO;
import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.Check;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.cve.log.Log.args;
import static java.sql.Types.*;

import static com.cve.util.Check.notNull;

/**
 * Skeletal implementation of DB meta data reader.
 * This class is designed to be extended.
 * Ideally, this would be a value class like DBResultSetMetaData.
 * Unfortunately, it takes a long time for most DBMSs to generate all of their
 * meta data and give it to us.
 * Also, if it were one big persisted value, then the whole thing would have
 * to be obtained for every request -- despite the fact that any given
 * request would only use a tiny bit of it.
 * @author curt
 */
public class DefaultDBMetaData implements DBMetaData {

    final ManagedFunction.Factory managedFunction;

    final DBServersStore serversStore;

    public final DBMetaDataIO dbmd;

    protected DefaultDBMetaData(DBMetaDataIO dbmd, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        this.dbmd = notNull(dbmd);
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
    }

    public static DBMetaData getDbmd(DefaultDBConnection connection, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        args(connection);
        DBMetaData meta = getDbmd0(connection,managedFunction,serversStore);
        meta = DBMetaDataLogger.of(System.out,meta);
        //meta = DBMetaDataCache.of(meta);
        //meta = DBMetaDataTimer.of(meta);
        //meta = DBMetaDataLocked.of(meta);
        return meta;
    }

    private static DBMetaData getDbmd0(DefaultDBConnection connection, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        Check.notNull(connection);
        DBDriver driver = connection.getInfo().driver;
        DBMetaDataIO io = DefaultDBMetaDataIO.of(connection, managedFunction);
        DBMetaData meta = driver.getDBMetaData(io,managedFunction,serversStore);
        return meta;
    }


    @Override
    public CurrentValue<ImmutableList<DBColumn>> getPrimaryKeysFor(ImmutableList<DBTable> tables) {
        args(tables);
        Set<DBColumn> keys = Sets.newHashSet();
        for (DBTable table : tables) {
            keys.addAll(getPrimaryKeysFor(table));
        }
        return CurrentValue.of(ImmutableList.copyOf(keys));
    }

    /**
     * See http://java.sun.com/javase/6/docs/api/java/sql/DatabaseMetaData.html#getPrimaryKeys(java.lang.String,%20java.lang.String,%20java.lang.String)
     */
    private ImmutableList<DBColumn> getPrimaryKeysFor(DBTable table) {
        args(table);
        Database     database = table.database;
        DBServer         server = database.server;
        String        catalog = database.name;
        String         schema = null;
        String      tableName = table.name;
        List<DBColumn> list = Lists.newArrayList();
        KeySpecifier specifier = KeySpecifier.of(catalog, schema, tableName);
        for (PrimaryKeyInfo info : dbmd.getPrimaryKeys(specifier).value) {
            list.add(table.keyColumnName(info.columnName));
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return columns;
    }


    /**
     * Return all the potential joins from the columns in the given tables.
     */
    @Override
    public CurrentValue<ImmutableList<Join>> getJoinsFor(ImmutableList<DBTable> tables) {
        args(tables);
        Set<Join> joins = Sets.newLinkedHashSet();
        for (DBTable table : tables) {
            joins.addAll(getImportedKeysFor(table));
            joins.addAll(getExportedKeysFor(table));
            joins.addAll(getReasonableJoinsFor(table));
        }
        ImmutableList<Join> copy = ImmutableList.copyOf(joins);
        return CurrentValue.of(copy);
    }

    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(ImmutableList<DBTable> tables) {
        args(tables);
        Set<DBColumn> set = Sets.newHashSet();
        for (DBTable table : tables) {
            set.addAll(getColumnsFor(table).value);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(set);
        return CurrentValue.of(columns);
    }

    @Override
    public CurrentValue<Long> getRowCountFor(DBTable table) {
        args(table);
        DBServer           server = table.database.server;
        DBConnection connection = DBConnectionFactory.getConnection(server,serversStore,managedFunction);
        SQL sql = SQL.of("SELECT count(*) FROM " + table.fullName());
        DBResultSetIO results = connection.select(sql).value;
        return CurrentValue.of(new Long(results.getLong(0,1)));
    }

    /**
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBServer server) {
        args(server);
        List<DBColumn> list = Lists.newArrayList();
        for (Database database : getDatabasesOn(server).value) {
            String          catalog = database.name;
            String    schemaPattern = null;
            String tableNamePattern = null;
            String columnNamePattern = null;
            ColumnSpecifier specifier = ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            for (ColumnInfo info : dbmd.getColumns(specifier).value ) {
                String  tableName = info.tableName; 
                String columnName = info.columnName;
                Class        type = classFor(info.dataType); 
                DBTable     table = database.tableName(tableName);
                Keyness   keyness = keyness(table,columnName);
                DBColumn column = table.keynessColumnNameType(keyness,columnName,type);
                list.add(column);
            }
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return CurrentValue.of(columns);
    }

        /**
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(Database database) {
        args(database);
        List<DBColumn> list = Lists.newArrayList();
        String          catalog = database.name;
        String    schemaPattern = null;
        String tableNamePattern = null;
        String columnNamePattern = null;
        ColumnSpecifier specifier = ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        for (ColumnInfo info : dbmd.getColumns(specifier).value ) {
            String  tableName = info.tableName;
            String columnName = info.columnName;
            Class        type = classFor(info.dataType);
            DBTable     table = database.tableName(tableName);
            Keyness   keyness = keyness(table,columnName);
            DBColumn column = table.keynessColumnNameType(keyness,columnName,type);
            list.add(column);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return CurrentValue.of(columns);
    }

    /**
     * Simple cache
     */
    @Override
    public CurrentValue<ImmutableList<DBColumn>> getColumnsFor(DBTable table) {
        args(table);
        Database       database = table.database;
        DBServer           server = database.server;
        String          catalog = database.name;
        String    schemaPattern = null;
        String tableNamePattern = table.name;
        String columnNamePattern = null;
        List<DBColumn> list = Lists.newArrayList();
        ColumnSpecifier specifier = ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        for (ColumnInfo info : dbmd.getColumns(specifier).value ) {
            String columnName = info.columnName;
            Class        type = classFor(info.dataType);
            Keyness   keyness = keyness(table,columnName);
            DBColumn column = table.keynessColumnNameType(keyness,columnName,type);
            list.add(column);
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return CurrentValue.of(columns);
    }

    /**
     * Return the keyness of the column name in the given table.
     * Right now 2009/09/15 the driver H2 has a bug that prevents this from working.
     * It works with MySQL.
     */
    public Keyness keyness(DBTable table, String columnName) {
        for (DBColumn column : getPrimaryKeysFor(table)) {
            if (columnName.equals(column.name)) {
                return Keyness.PRIMARY;
            }
        }
        for (Join join : getImportedKeysFor(table)) {
            DBColumn column = join.dest;
            if (columnName.equals(column.name)) {
                return Keyness.FOREIGN;
            }
        }
        return Keyness.NONE;
    }

    @Override
    public CurrentValue<ImmutableList<Database>> getDatabasesOn(DBServer server) {
        args(server);
        List<Database> list = Lists.newArrayList();
        for (CatalogInfo info : dbmd.getCatalogs().value) {
            String databaseName = info.databaseName;
            list.add(server.databaseName(databaseName));
        }
        ImmutableList<Database> databases = ImmutableList.copyOf(list);
        return CurrentValue.of(databases);
    }

    /**
     */
    @Override
    public CurrentValue<ImmutableList<DBTable>> getTablesOn(Database database) {
        args(database);
        String          catalog = database.name;
        String    schemaPattern = null;
        String tableNamePattern = null;
        String[]          types = null;
        List<DBTable> list = Lists.newArrayList();
        for (TableInfo info : dbmd.getTables(TableSpecifier.of(catalog, schemaPattern, tableNamePattern, types)).value) {
            String tableName = info.tableName;
            list.add(database.tableName(tableName));
        }
        ImmutableList<DBTable> tables = ImmutableList.copyOf(list);
        return CurrentValue.of(tables);
    }

    DBMetaDataIO getDbmdIO(DBServer server) {
        args(server);
        return DBConnectionFactory.getDbmdIO(server,serversStore,managedFunction);
    }

    /**
     */
    public ImmutableList<Join> getImportedKeysFor(DBTable table) {
        args(table);
        Database       database = table.database;
        DBServer           server = database.server;
        String   catalog = database.name;
        String    schema = null;
        String tableName = table.name;
        List<Join> list = Lists.newArrayList();
        KeySpecifier specifier = KeySpecifier.of(catalog, schema, tableName);
        for (ReferencedKeyInfo info : dbmd.getImportedKeys(specifier).value) {
            Database pkDatabase = server.databaseName(info.fkDatabase);
            Database fkDatabase = server.databaseName(info.fkDatabase);
            DBTable       pkTable = pkDatabase.tableName(info.pkTable);
            DBTable       fkTable = fkDatabase.tableName(info.fkTable);
            DBColumn       source = DBColumn.keyTableName(pkTable, info.pkColumn);
            DBColumn         dest = DBColumn.foreignkeyTableName(fkTable, info.fkColumn);
            list.add(Join.of(source, dest));
        }
        ImmutableList<Join> joins = ImmutableList.copyOf(list);
        return joins;
    }

    /**
     */
    public ImmutableList<Join> getExportedKeysFor(DBTable table) {
        args(table);
        Database       database = table.database;
        DBServer           server = database.server;
        String   catalog = database.name;
        String    schema = null;
        String tableName = table.name;
        List<Join> list = Lists.newArrayList();
        KeySpecifier specifier = KeySpecifier.of(catalog, schema, tableName);
        for (ReferencedKeyInfo info : dbmd.getExportedKeys(specifier).value) {
            Database pkDatabase = server.databaseName(info.fkDatabase);
            Database fkDatabase = server.databaseName(info.fkDatabase);
            DBTable       pkTable = pkDatabase.tableName(info.pkTable);
            DBTable       fkTable = fkDatabase.tableName(info.fkTable);
            DBColumn       source = DBColumn.keyTableName(pkTable, info.pkColumn);
            DBColumn         dest = DBColumn.foreignkeyTableName(fkTable, info.fkColumn);
            list.add(Join.of(source, dest));
        }
        ImmutableList<Join> joins = ImmutableList.copyOf(list);
        return joins;
    }

    /**
     * What is a reasonable join?
     * A join is reasonable, if both columns are on the same server and
     * have the same name.
     */
    private ImmutableList<Join> getReasonableJoinsFor(DBTable table) {
        args(table);
        Database database = table.database;
        DBServer     server = database.server;

        Multimap<String,DBColumn> columns = HashMultimap.create();
        for (DBColumn column : getColumnsFor(server).value) {
            columns.put(column.name, column);
        }

        Set<Join> joins = Sets.newLinkedHashSet();
        for (DBColumn source : getColumnsFor(table).value) {
            for (DBColumn dest : columns.get(source.name)) {
                if (!source.equals(dest)) {
                    joins.add(Join.of(source, dest));
                }
            }
        }
        return ImmutableList.copyOf(joins);
    }

    /**
     * See java.sql.Types
     * Don't trust these mappings.  They haven't been researched.
     * The void values below represent mappings I haven't even guessed at, yet.
     */
    private static Map<Integer,Class> KNOWN_TYPES = new HashMap() {{
        put(ARRAY,          Object[].class);
        put(BIGINT,         Void.class);
        put(BINARY,         Void.class);
        put(BIT,            Void.class);
        put(BLOB,           Void.class);
        put(BOOLEAN,        Boolean.class);
        put(CHAR,           String.class);
        put(CLOB,           String.class);
        put(DATALINK,       Void.class);
        put(DATE,           java.sql.Date.class);
        put(DECIMAL,        Void.class);
        put(DISTINCT,       Void.class);
        put(DOUBLE,         Double.class);
        put(FLOAT,          Float.class);
        put(INTEGER,        Integer.class);
        put(JAVA_OBJECT,    Void.class);
        put(LONGNVARCHAR,   String.class);
        put(LONGVARBINARY,  Void.class);
        put(LONGVARCHAR,    String.class);
        put(NCHAR,          String.class);
        put(NCLOB,          String.class);
        put(NULL,           Void.class);
        put(NUMERIC,        Number.class);
        put(NVARCHAR,       String.class);
        put(OTHER,          Void.class);
        put(REAL,           Void.class);
        put(REF,            Void.class);
        put(ROWID,          Void.class);
        put(SMALLINT,       Short.class);
        put(SQLXML,         Void.class);
        put(STRUCT,         Void.class);
        put(TIME,           java.util.Date.class);
        put(TIMESTAMP,      Void.class);
        put(TINYINT,        Void.class);
        put(VARBINARY,      Void.class);
        put(VARCHAR,        String.class);
    }};

    /**
     * See java.sql.Types
     */
    public Class classFor(int value) {
        Class c = KNOWN_TYPES.get(value);
        if (c==null) {
            throw new IllegalArgumentException(value + " is not a known type");
        }
        return c;
    }
}
