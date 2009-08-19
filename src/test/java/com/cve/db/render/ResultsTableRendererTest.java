package com.cve.db.render;

import com.cve.db.Cell;
import com.cve.html.SimpleTooltip;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.db.CellValues;
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
import com.cve.html.CSS;
import static com.cve.util.Replace.bracketQuote;
import static com.cve.util.Replace.escapeQuotes;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

import static com.cve.html.HTML.*;
/**
 *
 * @author Curt
 */
public class ResultsTableRendererTest {


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

    public SelectResults resultsForShowTable() {
        Server           server = Server.uri(URIs.of("server"));
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        DBColumn              age = person.columnNameType("age", Integer.class);
        DBRow                 row = DBRow.FIRST;
        Select           select = Select.from(database,person,name);
        Value             value = Value.of("Smith");
        DBResultSet     resultSet = DBResultSet.of(database,person,name,row,value);
        DBColumn       familyName = database.tableName("family").columnNameType("familyName", String.class);
        Hints             hints = Hints.of(Join.of(name,familyName),age);
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
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,false);
        return results;
    }

    /**
     * One row, one column result set.
     * Database Customer
     * Table    Person
     * Column   name
     * Row      Smith
     * @return
     */
    private String renderedOnePersonTable() {
        SelectResults results = onePersonResults();
        ClientInfo     client = ClientInfo.of();
        String       rendered = ResultsTableRenderer.render(results,client);
        return rendered;
    }

    @Test
    public void fullPersonTableHTML() {
        String rendered = renderedOnePersonTable();
        String expected =
table(
    tr(td("Database : <a href=[/server/customer/]>customer</a>",CSS.DATABASE)) + // databases row
    tr(td("Table : <a href=[/server/customer/customer.person/]>person</a>",CSS.TABLE)) +  // tables row
    // column names row
    tr(td(
        Link.textTargetTip(
            Label.of("name"),
            URIs.of("/server/customer/customer.person/customer.person.name/"),
            SimpleTooltip.of(
                escapeQuotes(bracketQuote(
                    "name<table border><tr><td><a href=[join?customer.person.name=customer.family.familyName]>" +
                    "join with customer.family.familyName</a></td></tr></table>"
            )))).toString()
    ,CSS.COLUMN_JOIN))
    //            +
    //tr(td("<a href=[hide?customer.person.name]>x</a>"),CSS.HIDE) + // column hide row
    //tr(td("<a href=[filter?customer.person.name=Smith]>Smith</a>"),CSS.ODD_ROW) // values rows
);
        expected = bracketQuote(expected);
        assertEquals(expected,rendered);
    }

    @Test
    public void personDatabaseRow() {
        String expected = td("Database : <a href=[/server/customer/]>customer</a>",1);
        expected = bracketQuote(expected);
        ClientInfo     client = ClientInfo.of();
        List list = ResultsTableRenderingTools.results(onePersonResults(),client).databaseRow();
        String rendered = list.get(0).toString();
        assertEquals(expected,rendered);
    }

    @Test
    public void personTableRow() {
        String expected = td("Table : <a href=[/server/customer/customer.person/]>person</a>",1);
        expected = bracketQuote(expected);
        ClientInfo     client = ClientInfo.of();
        List list = ResultsTableRenderingTools.results(onePersonResults(),client).tableRow();
        String rendered = list.get(0).toString();
        assertEquals(expected,rendered);
    }

    @Test
    public void personColumnNameRow() {
        String expected =
        td(
        "<a href=[/server/customer/customer.person/customer.person.name/] " +
        "onmouseover=[Tip('" +
             escapeQuotes(bracketQuote(
                 "name<table border><tr><td><a href=[join?customer.person.name=customer.family.familyName]>" +
                 "join with customer.family.familyName</a></td></tr></table>'")) +
        ", STICKY, 1)] onmouseout=[UnTip()]>name</a>"
        ,CSS.COLUMN_JOIN);
        expected = bracketQuote(expected);
        ClientInfo     client = ClientInfo.of();

        List list = ResultsTableRenderingTools.results(onePersonResults(),client).columnNameRow();
        String rendered = list.get(0).toString();
        assertEquals(expected,rendered);
    }

    @Test
    public void personColumnNameCell() {
        String expected =
        "<a href=[/server/customer/customer.person/customer.person.name/] " +
        "onmouseover=[Tip('" +
             escapeQuotes(bracketQuote(
                 "name<table border><tr><td><a href=[join?customer.person.name=customer.family.familyName]>" +
                 "join with customer.family.familyName</a></td></tr></table>'")) +
        ", STICKY, 1)] onmouseout=[UnTip()]>name</a>";
        expected = bracketQuote(expected);
        SelectResults results = onePersonResults();
        DBColumn         column = results.resultSet.columns.get(0);
        ClientInfo     client = ClientInfo.of();

        String rendered = ResultsTableRenderingTools.results(onePersonResults(),client).nameCell(column);
        assertEquals(expected,rendered);
    }

    @Test
    public void linkToColumn() {
        Server           server = Server.uri(URIs.of("server"));
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        String              uri = ResultsTableRenderingTools.linkTo(name).toString();
        // server / database / table / column
        assertEquals("/server/customer/customer.person/customer.person.name/",uri);
    }

    @Test
    public void personColumnHideRow() {
        String expected = bracketQuote(
            td("<a href=[hide?customer.person.name]>x</a>"));
        ClientInfo     client = ClientInfo.of();
        List list = ResultsTableRenderingTools.results(onePersonResults(),client).columnHideRow();
        String rendered = list.get(0).toString();
        assertEquals(expected,rendered);
    }

    @Test
    public void personValuesRows() {
        String expected = bracketQuote(
            tr(td("<a href=[filter?customer.person.name=Smith]>Smith</a>"),CSS.ODD_ROW));
        ClientInfo     client = ClientInfo.of();
        List list = ResultsTableRenderer.resultsClientInfo(onePersonResults(),client).valueRows();
        String rendered = list.get(0).toString();
        assertEquals(expected,rendered);
    }

    @Test
    public void personTableContainsDatabaseName() {
        String rendered = renderedOnePersonTable();
        assertTrue(rendered.contains("customer"));
    }

    @Test
    public void personTableContainsTableName() {
        String rendered = renderedOnePersonTable();
        assertTrue(rendered.contains("person"));
    }

    @Test
    public void personTableContainsHintedColumnName() {
        String rendered = renderedOnePersonTable();
        assertTrue(rendered.contains("familyName"));
    }

    @Test
    public void nameColumnCellContainsHintedColumnName() {
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
        ClientInfo     client = ClientInfo.of();

        String rendered = ResultsTableRenderingTools.results(results,client).nameCell(name);
        assertTrue(rendered,rendered.contains("familyName"));
    }

    @Test
    public void personTableContainsColumnName() {
        String rendered = renderedOnePersonTable();
        assertTrue(rendered,rendered.contains("name"));
    }

    @Test
    public void personTableContainsHTMLTabletags() {
        String rendered = renderedOnePersonTable();
        assertTrue(rendered.contains("table"));
        assertTrue(rendered.contains("<tr>"));
        assertTrue(rendered.contains("<td>"));
    }

    @Test
    public void personTableContainsBalancedBrackets() {
        String rendered = renderedOnePersonTable();
        int left  = countIn("<",rendered);
        int right = countIn(">",rendered);
        assertEquals(left,right);
    }

    /**
     * Database |          Geography
     * Table    |      City      |   State
     * Column   | name    | post | post | name
     *          |---------------------------------
     * Row      | Chicago | IL   | IL   | Illinois
     * Row      | Denver  | CO   | CO   | Colorado
     * @return
     */
    private String renderedCityStateTables() {
        Server           server = Server.uri(URIs.of("server"));
        Database       database = server.databaseName("geography");
        DBTable              city = database.tableName("city");
        DBTable             state = database.tableName("state");
        DBColumn         cityName = city.columnNameType("name",String.class);
        DBColumn         cityPost = city.columnNameType("post",String.class);
        DBColumn        stateName = state.columnNameType("name",String.class);
        DBColumn        statePost = state.columnNameType("post",String.class);
        Select           select = Select.from(database,city,state,cityName,cityPost,statePost,stateName);
        DBRow r1 = DBRow.FIRST;
        DBRow r2 = DBRow.number(1);
        DBResultSet     resultSet = DBResultSet.of(database,city,state,
                cityName, cityPost, statePost, stateName,
                r1,r2,
                CellValues.of(
                    cityName, cityPost, statePost, stateName,
                    r1, r2,
                    "Chicago","IL","IL","Illinois",
                    "Denver" ,"CO","CO","Colorado"
                )
        );

        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,Hints.NONE,false);
        ClientInfo     client = ClientInfo.of();

        String         rendered = ResultsTableRenderer.render(results,client);
        return rendered;
    }

    @Test
    public void cityStateTableContainsAll() {
        String rendered = renderedCityStateTables();
        assertTrue(rendered.contains("geography"));
        assertTrue(rendered.contains("city"));
        assertTrue(rendered.contains("state"));
        assertTrue(rendered.contains("name"));
        assertTrue(rendered.contains("post"));
        assertTrue(rendered.contains("Chicago"));
        assertTrue(rendered.contains("Illinois"));
        assertTrue(rendered.contains("Denver"));
        assertTrue(rendered.contains("Colorado"));
        assertTrue(rendered.contains("IL"));
        assertTrue(rendered.contains("CO"));
    }


    static int countIn(String find, String in) {
        int found = 0;
        for (int i=0; i<in.length(); i++) {
            if (find.equals(in.substring(i,i))) {
                found++;
            }
        }
        return found;
    }
}
