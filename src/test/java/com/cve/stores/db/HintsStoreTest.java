package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.Hints;
import com.cve.model.db.Join;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class HintsStoreTest {

    ;
    
    @Test
    public void findsHintsWithJoin() throws SQLException {
        DBServer         server = DBServer.uri(URIs.of("server"));

        Database         database = server.databaseName("DB1");
        DBTable             account = database.tableName("ACCOUNT");
        DBTable             product = database.tableName("PRODUCT");
        DBColumn account_product_id = DBColumn.tableNameType(account,"product_id",Integer.class);
        DBColumn product_product_id = DBColumn.tableNameType(product,"product_id",Integer.class);

        ImmutableList<DBColumn> columns = ImmutableList.of(account_product_id);

        Hints expected = Hints.of(Join.of(account_product_id, product_product_id));

        DBHintsStore store = MemoryDBHintsStore.of();
        store.put(columns,expected);

        Hints actual = store.get(columns);
        assertEquals(expected,actual);
    }

}
