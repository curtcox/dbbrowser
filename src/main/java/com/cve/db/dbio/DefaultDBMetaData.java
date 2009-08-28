package com.cve.db.dbio;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.stores.ServersStore;
import com.cve.util.Check;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Skeletal implementation of DB meta data reader.
 * This class is designed to be extended.
 * Ideally, this would be a value class like DBResultSetMetaData.
 * Unfortunately, it takes a long time for most DBMSs to generate all of their
 * meta data and give it to us.
 * @author curt
 */
class DefaultDBMetaData implements DBMetaData {

    protected DefaultDBMetaData() {}

    static DBMetaData getDbmd(DBConnection connection) {
        DBMetaData meta = getDbmd0(connection);
        meta = DBMetaDataLogger.of(System.out,meta);
        meta = DBMetaDataCache.of(meta);
        meta = DBMetaDataTimer.of(meta);
        return meta;
    }

    private static DBMetaData getDbmd0(DBConnection connection) {
        Check.notNull(connection);
        DBDriver driver = connection.info.driver;
        if (driver.equals(DBDriver.MySql)) {
            return new MySQLMetaData();
        }
        if (driver.equals(DBDriver.MsSqlTds)) {
            return DBMetaDataExceptionEater.of(MsSQLTdsMetaData.of());
        }
        if (driver.equals(DBDriver.H2)) {
            return H2MetaData.of();
        }
        throw new IllegalArgumentException(driver.toString());
    }


    @Override
    public ImmutableList<DBColumn> getPrimaryKeysFor(ImmutableList<DBTable> tables) throws SQLException {
        Set<DBColumn> keys = Sets.newHashSet();
        for (DBTable table : tables) {
            keys.addAll(getPrimaryKeysFor(table));
        }
        return ImmutableList.copyOf(keys);
    }

    private static ImmutableList<DBColumn> getPrimaryKeysFor(DBTable table) throws SQLException {
        Database     database = table.database;
        Server         server = database.server;
        DBMetaDataIO     dbmd = getDbmdIO(server);
        String        catalog = database.name;
        String         schema = null;
        String      tableName = table.name;
        ResultSet results = dbmd.getPrimaryKeys(catalog, schema, tableName);
        try {
            List<DBColumn> list = Lists.newArrayList();
            while (results.next()) {
                String columnName = results.getString("COLUMN_NAME");
                list.add(table.columnName(columnName));
            }
            ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }


    /**
     * Return all the potential joins from the columns in the given tables.
     */
    @Override
    public ImmutableList<Join> getJoinsFor(ImmutableList<DBTable> tables)  throws SQLException {
        Set<Join> joins = Sets.newLinkedHashSet();
        for (DBTable table : tables) {
            joins.addAll(getImportedKeysFor(table));
            joins.addAll(getExportedKeysFor(table));
            joins.addAll(getReasonableJoinsFor(table));
        }
        return ImmutableList.copyOf(joins);
    }

    @Override
    public ImmutableList<DBColumn> getColumnsFor(ImmutableList<DBTable> tables)  throws SQLException {
        Set<DBColumn> set = Sets.newHashSet();
        for (DBTable table : tables) {
            set.addAll(getColumnsFor(table));
        }
        return ImmutableList.copyOf(set);
    }

    /**
     */
    @Override
    public ImmutableList<DBColumn> getColumnsFor(Server server)  throws SQLException {
        DBMetaDataIO   dbmd = getDbmdIO(server);
        List<DBColumn> list = Lists.newArrayList();
        for (Database database : getDatabasesOn(server)) {
            String          catalog = database.name;
            String    schemaPattern = null;
            String tableNamePattern = null;
            String columnNamePattern = null;
            ResultSet results = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            try {
                while (results.next()) {
                    String tableName = results.getString("TABLE_NAME");
                    String columnName = results.getString("COLUMN_NAME");
                    DBColumn column = database.tableName(tableName).columnName(columnName);
                    list.add(column);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                close(results);
            }
        }
        ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
        return columns;
    }

    /**
     * Simple cache
     */
    @Override
    public ImmutableList<DBColumn> getColumnsFor(DBTable table)  throws SQLException {
        Database       database = table.database;
        Server           server = database.server;
        DBMetaDataIO       dbmd = getDbmdIO(server);
        String          catalog = database.name;
        String    schemaPattern = null;
        String tableNamePattern = table.name;
        String columnNamePattern = null;
        ResultSet results = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try {
            List<DBColumn> list = Lists.newArrayList();
            while (results.next()) {
                String columnName = results.getString("COLUMN_NAME");
                list.add(table.columnName(columnName));
            }
            ImmutableList<DBColumn> columns = ImmutableList.copyOf(list);
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    @Override
    public ImmutableList<Database> getDatabasesOn(Server server)  throws SQLException {
        DBMetaDataIO     dbmd = getDbmdIO(server);
        ResultSet results = dbmd.getCatalogs();
        try {
            List<Database> list = Lists.newArrayList();
            while (results.next()) {
                String databaseName = results.getString("TABLE_CAT");
                list.add(server.databaseName(databaseName));
            }
            ImmutableList<Database> databases = ImmutableList.copyOf(list);
            return databases;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    /**
     */
    @Override
    public ImmutableList<DBTable> getTablesOn(Database database)  throws SQLException {
        Server           server = database.server;
        DBMetaDataIO           dbmd = getDbmdIO(server);
        String          catalog = database.name;
        String    schemaPattern = null;
        String tableNamePattern = null;
        String[]          types = null;
        ResultSet results = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);
        try {
            List<DBTable> list = Lists.newArrayList();
            while (results.next()) {
                String tableName = results.getString("TABLE_NAME");
                list.add(database.tableName(tableName));
            }
            ImmutableList<DBTable> tables = ImmutableList.copyOf(list);
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
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

    public static DBMetaDataIO getDbmdIO(Server server) {
        DBConnection connection = ServersStore.getConnection(server);
        DBMetaDataIO   dbmd = DBMetaDataIO.connection(connection);
        return dbmd;
    }

    /**
     */
    public ImmutableList<Join> getImportedKeysFor(DBTable table)  throws SQLException {
        Database       database = table.database;
        Server           server = database.server;
        DBMetaDataIO    dbmd = getDbmdIO(server);
        String   catalog = database.name;
        String    schema = null;
        String tableName = table.name;
        ResultSet results = dbmd.getImportedKeys(catalog, schema, tableName);
        try {
            List<Join> list = Lists.newArrayList();
            while (results.next()) {
                Database pkDatabase = server.databaseName(results.getString("PKTABLE_CAT"));
                Database fkDatabase = server.databaseName(results.getString("FKTABLE_CAT"));
                DBTable       pkTable = pkDatabase.tableName(results.getString("PKTABLE_NAME"));
                DBTable       fkTable = fkDatabase.tableName(results.getString("FKTABLE_NAME"));
                DBColumn       source = DBColumn.tableName(pkTable, results.getString("PKCOLUMN_NAME"));
                DBColumn         dest = DBColumn.tableName(fkTable, results.getString("FKCOLUMN_NAME"));
                list.add(Join.of(source, dest));
            }
            ImmutableList<Join> joins = ImmutableList.copyOf(list);
            return joins;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    /**
     */
    public static ImmutableList<Join> getExportedKeysFor(DBTable table)  throws SQLException {
        Database       database = table.database;
        Server           server = database.server;
        DBMetaDataIO    dbmd = getDbmdIO(server);
        String   catalog = database.name;
        String    schema = null;
        String tableName = table.name;
        ResultSet results = dbmd.getExportedKeys(catalog, schema, tableName);
        try {
            List<Join> list = Lists.newArrayList();
            while (results.next()) {
                Database pkDatabase = server.databaseName(results.getString("PKTABLE_CAT"));
                Database fkDatabase = server.databaseName(results.getString("FKTABLE_CAT"));
                DBTable       pkTable = pkDatabase.tableName(results.getString("PKTABLE_NAME"));
                DBTable       fkTable = fkDatabase.tableName(results.getString("FKTABLE_NAME"));
                DBColumn       source = DBColumn.tableName(pkTable, results.getString("PKCOLUMN_NAME"));
                DBColumn         dest = DBColumn.tableName(fkTable, results.getString("FKCOLUMN_NAME"));
                list.add(Join.of(source, dest));
            }
            ImmutableList<Join> joins = ImmutableList.copyOf(list);
            return joins;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(results);
        }
    }

    /**
     * What is a reasonable join?
     * A join is reasonable, if both columns are on the same server and
     * have the same name.
     */
    private ImmutableList<Join> getReasonableJoinsFor(DBTable table)  throws SQLException {
        Database database = table.database;
        Server     server = database.server;

        Multimap<String,DBColumn> columns = HashMultimap.create();
        for (DBColumn column : getColumnsFor(server)) {
            columns.put(column.name, column);
        }

        Set<Join> joins = Sets.newLinkedHashSet();
        for (DBColumn source : getColumnsFor(table)) {
            for (DBColumn dest : columns.get(source.name)) {
                joins.add(Join.of(source, dest));
            }
        }
        return ImmutableList.copyOf(joins);
    }

}
