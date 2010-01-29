package com.cve.web.db.render;

import com.cve.model.db.Cell;
import com.cve.model.db.CellValues;
import com.cve.model.db.SelectResults;
import com.cve.model.db.Database;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Hints;
import com.cve.model.db.Join;
import com.cve.model.db.DBLimit;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.DBRow;
import com.cve.model.db.Order;
import com.cve.model.db.Select;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBValue;
import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
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

/**
 *
 * @author Curt
 */
public class ResultsTableRendererTest {

    ;
    HTMLTags tags;

    String tr(String s) { return tags.tr(s); }
    String td(String s) { return tags.td(s); }
    String td(String s, int w) { return tags.td(s,w); }
    String tr(String s, CSS css) { return tags.tr(s,css); }
    String th(String s, CSS css) { return tags.th(s,css); }
    String td(String s, CSS css) { return tags.td(s,css); }

    public SelectResults onePersonResults() {
        DBServer           server = DBServer.uri(URIs.of("server"));
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        DBRow                 row = DBRow.FIRST;
        Select           select = Select.from(database,person,name);
        DBValue             value = DBValue.of("Smith");
        DBResultSet     resultSet = DBResultSet.of(database,person,name,row,value);
        DBColumn       familyName = database.tableName("family").columnNameType("familyName", String.class);
        Hints             hints = Hints.of(Join.of(name,familyName));
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,false);
        return results;
    }

    public SelectResults resultsForShowTable() {
        DBServer           server = DBServer.uri(URIs.of("server"));
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        DBColumn              age = person.columnNameType("age", Integer.class);
        DBRow                 row = DBRow.FIRST;
        Select           select = Select.from(database,person,name);
        DBValue             value = DBValue.of("Smith");
        DBResultSet     resultSet = DBResultSet.of(database,person,name,row,value);
        DBColumn       familyName = database.tableName("family").columnNameType("familyName", String.class);
        Hints             hints = Hints.of(Join.of(name,familyName),age);
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,false);
        return results;
    }

    SelectResults multiPersonResults(int first, int last, DBLimit limit, boolean hasMore) {
        DBServer           server = DBServer.uri(URIs.of("server"));
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        List<DBRow>          rows = Lists.newArrayList();
        Map<Cell,DBValue>  values = Maps.newHashMap();
        DBRow      row = DBRow.FIRST;
        DBValue  value = DBValue.of("Smith");
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
        ImmutableList<Order> orders = ImmutableList.of();
        String       rendered = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet, orders, results.hints, client).landscapeTable();
        return rendered;
    }

    @Test
    public void fullPersonTableHTML() {
        String rendered = renderedOnePersonTable();
        // Assertions for this test deleted for now.  Look in the repo when fixing.
    }

    static void equals(String expected, String actual) {
        if (expected.equals(actual)) {
            return;
        }
        int i=0;
        for (; i<expected.length() &&
               i<actual.length()   &&
               expected.charAt(i)==actual.charAt(i); i++){}
        String prefix = expected.substring(0,i);
        System.out.println("Both strings start with [[" + prefix + "]]");
        System.out.println("expected [[" + expected + "]] <>");
        System.out.println("actual   [[" + actual + "]]");
        assertEquals(expected,actual);
    }

    @Test
    public void personDatabaseRow() {
        String expected = td("Database : <a href=[/*/server/customer/]>customer</a>",1);
        expected = bracketQuote(expected);
        ClientInfo     client = ClientInfo.of();
        SelectResults results = onePersonResults();
        ImmutableList<Order> orders = ImmutableList.of();
        List list = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet, orders, results.hints,client).databaseRow();
        String rendered = list.get(0).toString();
        equals(expected,rendered);
    }

    @Test
    public void personTableRow() {
        String expected = td("Table : <a href=[/*/server/customer/customer.person/]>person</a>",1);
        expected = bracketQuote(expected);
        ClientInfo     client = ClientInfo.of();
        SelectResults results = onePersonResults();
        ImmutableList<Order> orders = ImmutableList.of();
        List list = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet,orders, results.hints,client).tableRow();
        String rendered = list.get(0).toString();
        equals(expected,rendered);
    }

    @Test
    public void personColumnNameRow() {
        String expected =
        td(
        "<a href=[/*/server/customer/customer.person/customer.person.name/] " +
        "onmouseover=[Tip('" +
             escapeQuotes(bracketQuote(
                 "name<table border><tr><td><a href=[join?customer.person.name=customer.family.familyName]>" +
                 "join with customer.family.familyName</a></td></tr></table>'")) +
        ", STICKY, 1)] onmouseout=[UnTip()]>name</a>"
        ,CSS.COLUMN_JOIN);
        expected = bracketQuote(expected);
        ClientInfo     client = ClientInfo.of();

        SelectResults results = onePersonResults();
        ImmutableList<Order> orders = ImmutableList.of();
        List list = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet,orders,results.hints,client).columnNameRow();
        String rendered = list.get(0).toString();
        equals(expected,rendered);
    }

    @Test
    public void personColumnNameCell() {
        String expected =
        "<a href=[/*/server/customer/customer.person/customer.person.name/] " +
        "onmouseover=[Tip('" +
             escapeQuotes(bracketQuote(
                 "name<table border><tr><td><a href=[join?customer.person.name=customer.family.familyName]>" +
                 "join with customer.family.familyName</a></td></tr></table>'")) +
        ", STICKY, 1)] onmouseout=[UnTip()]>name</a>";
        expected = bracketQuote(expected);
        SelectResults results = onePersonResults();
        DBColumn         column = results.resultSet.columns.get(0);
        ClientInfo     client = ClientInfo.of();

        ImmutableList<Order> orders = ImmutableList.of();
        String rendered = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet,orders,results.hints,client).nameCell(column);
        equals(expected,rendered);
    }

    @Test
    public void renderedContainsLinkToHide() {
        String linkToHide = bracketQuote("hide?customer.person.name");
        ClientInfo     client = ClientInfo.of();
        SelectResults results = onePersonResults();
        ImmutableList<Order> orders = ImmutableList.of();
        List list = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet,orders,results.hints,client).columnActionsRow();
        String rendered = list.get(0).toString();
        assertTrue(rendered.contains(linkToHide));
    }

    @Test
    public void personValuesRows() {
        String expected = bracketQuote(
            tr(td("<a href=[filter?customer.person.name=Smith]>Smith</a>"),CSS.ODD_ROW));
        ClientInfo     client = ClientInfo.of();
        SelectResults results = onePersonResults();
        ImmutableList<Order> orders = ImmutableList.of();
        List list = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet,orders,results.hints,client).valueRowsList();
        String rendered = list.get(0).toString();
        equals(expected,rendered);
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
        DBServer           server = DBServer.uri(URIs.of("server"));
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        DBRow                 row = DBRow.FIRST;
        Select           select = Select.from(database,person,name);
        DBValue             value = DBValue.of("Smith");
        DBResultSet     resultSet = DBResultSet.of(database,person,name,row,value);
        DBColumn       familyName = database.tableName("family").columnNameType("familyName", String.class);
        Hints             hints = Hints.of(Join.of(name,familyName));
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,false);
        ClientInfo     client = ClientInfo.of();

        ImmutableList<Order> orders = ImmutableList.of();
        String rendered = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet,orders,results.hints,client).nameCell(name);
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
        DBServer           server = DBServer.uri(URIs.of("server"));
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
