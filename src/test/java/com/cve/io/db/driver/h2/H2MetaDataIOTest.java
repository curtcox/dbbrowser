package com.cve.io.db.driver.h2;

import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DBMetaDataIO.ColumnInfo;
import com.cve.io.db.DBMetaDataIO.ColumnSpecifier;
import com.cve.io.db.DefaultDBConnection;
import com.cve.log.Log;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBServer;
import com.cve.sample.db.SampleH2Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class H2MetaDataIOTest {

    ;
    final DBServer server = SampleH2Server.SAMPLE;
    final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    final DBServersStore serversStore = MemoryDBServersStore.of();
    final DBConnectionInfo connectionInfo = SampleH2Server.getConnectionInfo();
    final DefaultDBConnection connection = DefaultDBConnection.of(connectionInfo,serversStore,managedFunction);

    final DBMetaDataIO io = H2MetaDataIO.of(connection, managedFunction);

    @Test
    public void getColumns() {
        String catalog = null;
        String schemaPattern = null;
        String tableNamePattern = null;
        String columnNamePattern = null;
        ColumnSpecifier specifier = ColumnSpecifier.of(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        ImmutableList<ColumnInfo> columns = io.getColumns(specifier).value;
        assertTrue(columns.size()>350);

        for (ColumnInfo info : columns) {
            String    tableName = info.tableName;
            String   columnName = info.columnName;
            String databaseName = info.tableSchema;
            int            type = info.dataType;
            assertNotNull(tableName);
            assertNotNull(columnName);
            assertNotNull(databaseName);
        }
    }

    public static void main(String[] args) {
        H2MetaDataIOTest test = new H2MetaDataIOTest();
        test.getColumns();
        System.out.println("Done.");
    }
}
