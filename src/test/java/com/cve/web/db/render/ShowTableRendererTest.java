package com.cve.web.db.render;

import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.model.db.Cell;
import com.cve.model.db.SelectResults;
import com.cve.model.db.Database;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Hints;
import com.cve.model.db.Join;
import com.cve.model.db.DBLimit;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.DBRow;
import com.cve.model.db.Select;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBValue;
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

/**
 *
 * @author Curt
 */
public class ShowTableRendererTest {

    Log log;

    HTMLTags tags;

    public SelectResults onePersonResults() {
        DBServer           server = DBServer.uri(URIs.of("server"),log);
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        DBRow                 row = DBRow.FIRST;
        Select           select = Select.from(database,person,name);
        DBValue             value = DBValue.of("Smith");
        DBResultSet     resultSet = DBResultSet.of(database,person,name,row,value,log);
        DBColumn       familyName = database.tableName("family").columnNameType("familyName", String.class);
        Hints             hints = Hints.of(Join.of(name,familyName));
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,false);
        return results;
    }

    public SelectResults resultsForShowTable() {
        DBServer           server = DBServer.uri(URIs.of("server"),log);
        Database       database = server.databaseName("customer");
        DBTable            person = database.tableName("person");
        DBColumn             name = person.columnNameType("name",String.class);
        DBColumn              age = person.columnNameType("age", Integer.class);
        DBRow                 row = DBRow.FIRST;
        Select           select = Select.from(database,person,name);
        DBValue             value = DBValue.of("Smith");
        DBResultSet     resultSet = DBResultSet.of(database,person,name,row,value,log);
        DBColumn       familyName = database.tableName("family").columnNameType("familyName", String.class);
        Hints             hints = Hints.of(Join.of(name,familyName),age);
        SelectResults   results = SelectResults.selectResultsHintsMore(select,resultSet,hints,false);
        return results;
    }

    SelectResults multiPersonResults(int first, int last, DBLimit limit, boolean hasMore) {
        DBServer           server = DBServer.uri(URIs.of("server"),log);
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
        DBResultSet     resultSet = DBResultSet.of(database,person,name,ImmutableList.copyOf(rows),ImmutableMap.copyOf(values),log);
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
            tags.borderTable(
                tags.tr("<td rowspan=[1]>Show</td><td>customer.person</td><td><a href=[show?customer.person.age]>age</a> </td>")
            ));
        String rendered = ShowTableRenderer.results(resultsForShowTable()).showTable();
        assertEquals(expected,rendered);
    }


}
