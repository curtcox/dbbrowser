package com.cve.web.db.render;

import com.cve.html.HTML;
import com.cve.log.Log;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Join;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBValue;
import com.cve.util.Replace;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import static org.junit.Assert.*;

import static com.cve.web.db.render.ColumnNameTooltip.*;

/**
 *
 * @author curt
 */
public class ColumnNameTooltipTest {

    Log log;

    public ColumnNameTooltipTest() {}

    private Join getJoin() {
        DBServer     server = DBServer.uri(URIs.of("server"),log);
        Database database = server.databaseName("db");
        DBTable       table = DBTable.databaseName(database, "table",log);
        DBColumn        foo = DBColumn.tableNameType(table, "foo", String.class);
        DBColumn        bar = DBColumn.tableNameType(table, "bar", String.class);
        Join         join = Join.of(foo, bar);
        return join;
    }

    private DBRowFilter getFilter() {
        DBServer     server = DBServer.uri(URIs.of("server"),log);
        Database database = server.databaseName("db");
        DBTable       table = DBTable.databaseName(database, "table",log);
        DBColumn        foo = DBColumn.tableNameType(table, "foo", String.class);
        DBValue       value = DBValue.of("active");
        DBRowFilter     filter = DBRowFilter.of(foo,value);
        return filter;
    }

    private HTML getTip() {
        DBServer     server = DBServer.uri(URIs.of("server"),log);
        Database database = server.databaseName("db");
        DBTable       table = DBTable.databaseName(database, "table",log);
        DBColumn        foo = DBColumn.tableNameType(table, "foo", String.class);
        DBColumn        bar = DBColumn.tableNameType(table, "bar", String.class);
        Join         join = Join.of(foo, bar);
        DBValue       value = DBValue.of("active");
        DBRowFilter     filter = DBRowFilter.of(foo,value);

        ImmutableList<DBColumn>   joins = ImmutableList.of(bar);
        ImmutableList<DBRowFilter> filters = ImmutableList.of(filter);
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
        assertTrue(html.contains("foo"));
        assertTrue(html.contains(Replace.bracketQuote("<a href=[join?db.table.foo=db.table.bar]>join with db.table.bar</a>")));
        assertTrue(html.contains(Replace.bracketQuote("<a href=[filter?db.table.foo=active]>show only active</a>")));
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
