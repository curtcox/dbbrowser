package com.cve.web.db.render;

import com.cve.web.db.render.PagingLinksRenderer;
import com.cve.db.Cell;
import com.cve.db.SelectResults;
import com.cve.db.Database;
import com.cve.db.DBTable;
import com.cve.db.DBColumn;
import com.cve.db.Hints;
import com.cve.db.Join;
import com.cve.db.Limit;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.Select;
import com.cve.db.Server;
import com.cve.db.Value;
import static com.cve.util.Replace.bracketQuote;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class PagingLinksRendererTest {


    private static final String BACK = PagingLinksRenderer.BACK;
    private static final String NEXT = PagingLinksRenderer.NEXT;
    private static final String BIGGER = PagingLinksRenderer.BIGGER;

    public SelectResults onePersonResults() {
        Server           server = Server.uri(URIs.of("server"));
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        DBRow                 row = DBRow.FIRST;
        Select           select = Select.from(database,person,name);
        Value             value = Value.of("Smith");
        DBResultSet     resultSet = DBResultSet.of(database,person,name,row,value);
        DBColumn       familyName = database.tableName("family").columnNameType("familyName", String.class);
        Hints             hints = Hints.of(Join.of(name,familyName));
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,false);
        return results;
    }

    SelectResults multiPersonResults(int first, int last, Limit limit, boolean hasMore) {
        Server           server = Server.uri(URIs.of("server"));
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        List<DBRow>          rows = Lists.newArrayList();
        Map<Cell,Value>  values = Maps.newHashMap();
        DBRow      row = DBRow.FIRST;
        Value  value = Value.of("Smith");
        for (int i=first; i<last; i++) {
            Cell cell = Cell.at(row, name);
            rows.add(row);
            values.put(cell, value);
        }
        Select           select = Select.from(database,person,name).with(limit);
        DBResultSet     resultSet = DBResultSet.of(database,person,name,ImmutableList.copyOf(rows),ImmutableMap.copyOf(values));
        DBColumn       familyName = database.tableName("family").columnNameType("familyName", String.class);
        Hints             hints = Hints.of(Join.of(name,familyName));
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,hasMore);
        return results;
    }


    @Test
    public void personPagingLinksWhenNotMore() {
        String expected = "";
        String rendered = PagingLinksRenderer.results(onePersonResults()).pagingLinks();
        assertEquals(expected,rendered);
    }

    @Test
    public void personPagingLinksWhenMoreForwardButNotBack() {
        String expected = bracketQuote(
            "<a href=[next?1]><img alt=[Next rows] title=[Next rows] src=[/resource/icons/actions/go-next.png]></a>" +
            " <a href=[bigger?10]><img alt=[More rows] title=[More rows] src=[/resource/icons/actions/list-add.png]></a> "
        );
        Limit limit = Limit.DEFAULT;
        String rendered = PagingLinksRenderer.results(multiPersonResults(0,25,limit,true)).pagingLinks();
        assertEquals(expected,rendered);
    }

    @Test
    public void personPagingLinksWhenMoreBackButNotForward() {
        String expected = bracketQuote(
            "<a href=[back?1]><img alt=[Previous rows] title=[Previous rows] src=[/resource/icons/actions/go-previous.png]></a> ");
        Limit limit = Limit.limitOffset(10,5);
        String rendered = PagingLinksRenderer.results(multiPersonResults(5,25,limit,false)).pagingLinks();
        assertEquals(expected,rendered);
    }

    @Test
    public void personPagingLinksWhenMoreForwardAndBack() {
        String expected = bracketQuote(
            "<a href=[back?1]><img alt=[Previous rows] title=[Previous rows] src=[/resource/icons/actions/go-previous.png]></a>" +
            " <a href=[next?1]><img alt=[Next rows] title=[Next rows] src=[/resource/icons/actions/go-next.png]></a> <a href=[bigger?10]>"+
            "<img alt=[More rows] title=[More rows] src=[/resource/icons/actions/list-add.png]></a> "
            );
        Limit limit = Limit.limitOffset(10,5);
        String rendered = PagingLinksRenderer.results(multiPersonResults(5,25,limit,true)).pagingLinks();
        assertEquals(expected,rendered);
    }


}
