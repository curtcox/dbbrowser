package com.cve.db.render;

import com.cve.html.HTML;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Filter;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.Value;
import com.cve.util.Replace;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import static org.junit.Assert.*;

import static com.cve.db.render.ColumnNameTooltip.*;

/**
 *
 * @author curt
 */
public class ColumnNameTooltipTest {

    public ColumnNameTooltipTest() {}

    private Join getJoin() {
        Server     server = Server.uri(URIs.of("server"));
        Database database = server.databaseName("db");
        DBTable       table = DBTable.databaseName(database, "table");
        DBColumn        foo = DBColumn.tableNameType(table, "foo", String.class);
        DBColumn        bar = DBColumn.tableNameType(table, "bar", String.class);
        Join         join = Join.of(foo, bar);
        return join;
    }

    private Filter getFilter() {
        Server     server = Server.uri(URIs.of("server"));
        Database database = server.databaseName("db");
        DBTable       table = DBTable.databaseName(database, "table");
        DBColumn        foo = DBColumn.tableNameType(table, "foo", String.class);
        Value       value = Value.of("active");
        Filter     filter = Filter.of(foo,value);
        return filter;
    }

    private HTML getTip() {
        Server     server = Server.uri(URIs.of("server"));
        Database database = server.databaseName("db");
        DBTable       table = DBTable.databaseName(database, "table");
        DBColumn        foo = DBColumn.tableNameType(table, "foo", String.class);
        DBColumn        bar = DBColumn.tableNameType(table, "bar", String.class);
        Join         join = Join.of(foo, bar);
        Value       value = Value.of("active");
        Filter     filter = Filter.of(foo,value);

        ImmutableList<DBColumn>   joins = ImmutableList.of(bar);
        ImmutableList<Filter> filters = ImmutableList.of(filter);
        return ColumnNameTooltip.columnJoinsFilters(foo,joins,filters).toHTML();
    }

    @Test
    public void columnInfo() {
        DBColumn        foo = getJoin().source;
        String       html = ColumnNameTooltip.info(foo).toString();
        String expected = "foo";
        assertEquals(expected,html);
    }

    @Test
    public void joinInfo() {
        Join join = getJoin();
        String       html = ColumnNameTooltip.joinInfo(join.source,join.dest).toString();
        String expected = "<a href=[join?db.table.foo=db.table.bar]>join with db.table.bar</a>";
        expected = Replace.bracketQuote(expected);
        assertEquals(expected,html);
    }

    @Test
    public void filterInfo() {
        String       html = ColumnNameTooltip.info(getFilter()).toString();
        String expected = "<a href=[filter?db.table.foo=active]>show only active</a>";
        expected = Replace.bracketQuote(expected);
        assertEquals(expected,html);
    }

    @Test
    public void tipContents() {
        String html = getTip().toString();
        String expected = "foo" +
            table(
              tr(td("<a href=[join?db.table.foo=db.table.bar]>join with db.table.bar</a>")) +
              tr(td("<a href=[filter?db.table.foo=active]>show only active</a>"))
            );
        expected = Replace.bracketQuote(expected);
        assertEquals(expected,html);
    }

    @Test
    public void tipContainsColumn() {
        String html = getTip().toString();
        assertTrue(html,html.contains("foo"));
    }

    @Test
    public void tipContainsJoins() {
        String html = getTip().toString();
        assertTrue(html,html.contains("bar"));
    }

    @Test
    public void tipContainsfilters() {
        String html = getTip().toString();
        assertTrue(html,html.contains("active"));
    }

}
