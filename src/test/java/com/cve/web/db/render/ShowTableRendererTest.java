package com.cve.web.db.render;

import com.cve.web.db.render.ShowTableRenderer;
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
import com.cve.util.Replace;
import com.cve.util.URIs;
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
public class ShowTableRendererTest {


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
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,hasMore);
        return results;
    }

    @Test
    public void personShowTableWhenEmpty() {
        String expected = "";
        String rendered = ShowTableRenderer.results(onePersonResults()).showTable();
        assertEquals(expected,rendered);
    }

    @Test
    public void personShowTableWhenNotEmpty() {
        String expected = Replace.bracketQuote(
            borderTable(
                tr("<td rowspan=[1]>Show</td><td>customer.person</td><td><a href=[show?customer.person.age]>age</a> </td>")
            ));
        String rendered = ShowTableRenderer.results(resultsForShowTable()).showTable();
        assertEquals(expected,rendered);
    }


}
