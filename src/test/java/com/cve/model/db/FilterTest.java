package com.cve.model.db;

import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Database;
import com.cve.model.db.DBValue;
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
        assertEquals(column.filterValue(DBValue.of(object)),
                     column.filterValue(DBValue.of(object)));
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
        assertFalse(column.filterValue(DBValue.of(a)).equals(
                    column.filterValue(DBValue.of(b))));
    }

    private void assertRenderedFilterParses(Object value) {
        DBServer   server = DBServer.uri(URIs.of("server"));
        DBTable   table = server.databaseName("db").tableName("foo");
        DBRowFilter filter = table
                .columnName("bar")
                .filterValue(DBValue.of(value));
        ImmutableList<DBTable> tables = ImmutableList.of(table);
        String fragment = filter.toUrlFragment();
        System.out.println("value   =" + value);
        System.out.println("fragment=" + fragment);
        // We use "+" to separate filters in URLs, so the fragments can't
        // contain them
        assertFalse("Should not contain + : " + fragment,fragment.contains("+"));
        DBRowFilter parsed = DBRowFilter.parse(server, tables, fragment);
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