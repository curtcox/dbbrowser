package com.cve.db;

import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class SelectTest {

    public SelectTest() {
    }


    /**
     * Test of getSelect method, of class SimpleSelectResults.
     */
    @Test
    public void selectGetterEqualsConstructor() {
        DBServer server = DBServer.uri(URIs.of("server"));
        ImmutableList<Database>   databases = ImmutableList.of(server.databaseName("database"));
        ImmutableList<DBTable>       tables = ImmutableList.of();
        ImmutableList<DBColumn>     columns = ImmutableList.of();
        ImmutableList<AggregateFunction> functions = ImmutableList.of();
        ImmutableList<Join>           joins = ImmutableList.of();
        ImmutableList<DBRowFilter>       filters = ImmutableList.of();
        ImmutableList<Order>         orders = ImmutableList.of();
        ImmutableList<Group>         groups = ImmutableList.of();

        Select select = Select.from(databases,tables,columns,functions,joins,filters,orders,groups,DBLimit.DEFAULT);
        assertEquals(tables,     select.tables);
        assertEquals(columns,    select.columns);
        assertEquals(functions,  select.functions);
        assertEquals(joins,      select.joins);
        assertEquals(filters,    select.filters);
        assertEquals(orders,     select.orders);
        assertEquals(groups,     select.groups);
    }


}