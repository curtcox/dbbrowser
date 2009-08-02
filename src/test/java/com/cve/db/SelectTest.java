package com.cve.db;

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
        ImmutableList<Database>   databases = ImmutableList.of();
        ImmutableList<DBTable>       tables = ImmutableList.of();
        ImmutableList<DBColumn>     columns = ImmutableList.of();
        ImmutableList<AggregateFunction> functions = ImmutableList.of();
        ImmutableList<Join>           joins = ImmutableList.of();
        ImmutableList<Filter>       filters = ImmutableList.of();
        ImmutableList<Order>         orders = ImmutableList.of();
        ImmutableList<Group>         groups = ImmutableList.of();

        Select select = Select.from(databases,tables,columns,functions,joins,filters,orders,groups,Limit.DEFAULT);
        assertEquals(tables,     select.getTables());
        assertEquals(columns,    select.getColumns());
        assertEquals(functions,  select.getFunctions());
        assertEquals(joins,      select.getJoins());
        assertEquals(filters,    select.getFilters());
        assertEquals(orders,     select.getOrders());
        assertEquals(groups,     select.getGroups());
    }


}