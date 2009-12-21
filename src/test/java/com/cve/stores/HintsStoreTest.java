package com.cve.stores;

import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Hints;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.db.DBTable;
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

    @Test
    public void findsHintsWithJoin() throws SQLException {
        Server         server = Server.uri(URIs.of("server"));

        Database         database = server.databaseName("DB1");
        DBTable             account = database.tableName("ACCOUNT");
        DBTable             product = database.tableName("PRODUCT");
        DBColumn account_product_id = DBColumn.tableNameType(account,"product_id",Integer.class);
        DBColumn product_product_id = DBColumn.tableNameType(product,"product_id",Integer.class);

        ImmutableList<DBColumn> columns = ImmutableList.of(account_product_id);

        Hints expected = Hints.of(Join.of(account_product_id, product_product_id));
        HintsStore.putHints(expected);

        Hints actual = HintsStore.of(null).getHints(columns);
        assertEquals(expected,actual);
    }

}
