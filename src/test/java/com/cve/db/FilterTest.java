package com.cve.db;

import com.google.common.collect.ImmutableList;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class FilterTest {

    public FilterTest() {}

    @Test
    public void fooIsFoo() {
        assertEquality("foo");
    }

    @Test
    public void _7Is7() {
        assertEquality("7");
    }

    void assertEquality(Object object) {
        DBServer   server = DBServer.uri(URIs.of("server"));
        Database     db = server.databaseName("db");
        DBTable   table = db.tableName("foo");
        DBColumn column = table.columnName("bar");
        assertEquals(column.filterValue(Value.of(object)),
                     column.filterValue(Value.of(object)));
    }

    @Test
    public void fooIsNotBar() {
        assertUnequality("foo","bar");
    }

    @Test
    public void _6IsNot7() {
        assertUnequality("6","7");
    }

    void assertUnequality(Object a, Object b) {
        DBServer   server = DBServer.uri(URIs.of("server"));
        Database     db = server.databaseName("db");
        DBTable   table = db.tableName("foo");
        DBColumn column = table.columnName("bar");
        assertFalse(column.filterValue(Value.of(a)).equals(
                    column.filterValue(Value.of(b))));
    }

    private void assertRenderedFilterParses(Object value) {
        DBServer   server = DBServer.uri(URIs.of("server"));
        DBTable   table = server.databaseName("db").tableName("foo");
        Filter filter = table
                .columnName("bar")
                .filterValue(Value.of(value));
        ImmutableList<DBTable> tables = ImmutableList.of(table);
        String fragment = filter.toUrlFragment();
        System.out.println("value   =" + value);
        System.out.println("fragment=" + fragment);
        // We use "+" to separate filters in URLs, so the fragments can't
        // contain them
        assertFalse("Should not contain + : " + fragment,fragment.contains("+"));
        Filter parsed = Filter.parse(server, tables, fragment);
        System.out.println("filter=" + filter);
        System.out.println("parsed=" + parsed);
        assertEquals(filter,parsed);
    }

    @Test
    public void filterWithoutSpaces() {
        assertRenderedFilterParses("baz");
    }

    @Test
    public void filterWithSpace() {
        assertRenderedFilterParses("ba z");
    }

    @Test
    public void filterWithSpaces() {
        assertRenderedFilterParses("b a z");
    }

    @Test
    public void filterWithPlusses() {
        assertRenderedFilterParses("b+a+z");
    }

    @Test
    public void filterWithOnlySpace() {
        assertRenderedFilterParses("b a z");
    }

}