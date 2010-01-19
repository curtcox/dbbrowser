package com.cve.io.db.driver;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.DefaultDBConnection;
import com.cve.io.db.driver.h2.H2Driver;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.Database;
import com.cve.sample.db.SampleGeoDB;
import com.cve.sample.db.SampleH2Server;
import com.cve.stores.CurrentValue;
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
public class DefaultDBMetaDataTest {

    final ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
    final DBServersStore serversStore = MemoryDBServersStore.of();
    final DBConnectionInfo info = SampleH2Server.getConnectionInfo();
    final DefaultDBConnection connection = DefaultDBConnection.of(info,serversStore,managedFunction);
    final DBMetaData dbmd = H2Driver.of().getDBMetaData(connection,managedFunction,serversStore);

    @Test
    public void getColumnsForServer() {
        CurrentValue<ImmutableList<DBColumn>> columns = dbmd.getColumnsFor(SampleH2Server.SAMPLE);
        assertEquals(3,columns.value.size());
    }

    @Test
    public void getColumnsForTable() {
        CurrentValue<ImmutableList<DBColumn>> columns = dbmd.getColumnsFor(SampleGeoDB.CITIES);
        assertEquals(3,columns.value.size());
    }

    @Test
    public void getDatabasesOnServer() {
        CurrentValue<ImmutableList<Database>> databases = dbmd.getDatabasesOn(SampleH2Server.SAMPLE);
        assertEquals(3,databases.value.size());
    }

    @Test
    public void getRowCountForTable() {
        CurrentValue<Long> count = dbmd.getRowCountFor(SampleGeoDB.CITIES);
        assertEquals(new Long(3),((Long) count.value));
    }

    public static void main(String[] args) {
        DefaultDBMetaDataTest test = new DefaultDBMetaDataTest();
        test.getColumnsForServer();
        test.getColumnsForTable();
        test.getDatabasesOnServer();
        test.getRowCountForTable();
    }
}
