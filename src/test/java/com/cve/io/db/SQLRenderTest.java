package com.cve.io.db;

import com.cve.log.Log;
import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Join;
import com.cve.model.db.DBLimit;
import com.cve.model.db.Order;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBValue;
import com.cve.util.Replace;
import com.cve.util.URIs;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class SQLRenderTest {

    Log log;

    @Test
    public void renderSelectPerson() {
        DBServer      server = DBServer.uri(URIs.of("server"));
        Database  database = server.databaseName("customer");
        DBTable       person = database.tableName("person");
        DBColumn        name = person.columnNameType("name",String.class);
        DBRowFilter      filter = DBRowFilter.of(name, DBValue.of("Smith"),log);
        Select      select = Select.from(database,person,name,filter);
        SQL expected = SQL.of(Replace.bracketSingleQuote(
            "SELECT customer.person.name " +
            "FROM customer.person " +
            "WHERE customer.person.name=[Smith] LIMIT 21 OFFSET 0"));
        Search search = Search.EMPTY;
        SQL actual = new SimpleSelectRenderer().render(select,search);
        assertEquals(expected,actual);
    }

    @Test
    public void renderSelectPersonAccount() {
        DBServer        server = DBServer.uri(URIs.of("server"));
        Database    database = server.databaseName("customer");
        DBTable         person = database.tableName("person");
        DBTable        account = database.tableName("account");
        DBColumn          name = person.columnNameType("name",String.class);
        DBColumn  person_email = person.columnNameType("email",String.class);
        DBColumn account_email = account.columnNameType("email",String.class);
        Join            join = Join.of(person_email, account_email);
        DBRowFilter        filter = DBRowFilter.of(name, DBValue.of("Smith"),log);
        Order          order = Order.ascending(name);
        AggregateFunction self = AggregateFunction.IDENTITY;
        Select        select = Select.from(
            list(database),list(person,account),list(name),list(self),list(join),list(filter),list(order),list(),DBLimit.DEFAULT);
        SQL expected = SQL.of(Replace.bracketSingleQuote(
            "SELECT customer.person.name " +
            "FROM customer.person,customer.account " +
            "WHERE customer.person.email=customer.account.email " +
            "AND customer.person.name=[Smith] " +
            "ORDER BY customer.person.name ASC " +
            "LIMIT 21 OFFSET 0"
        ));
        Search search = Search.EMPTY;
        SQL actual = new SimpleSelectRenderer().render(select,search);
        assertEquals(expected,actual);
    }

    private static ImmutableList list() {
        return ImmutableList.of();
    }

    private static <T> ImmutableList<T> list(T one) {
        return ImmutableList.of(one);
    }

    private static <T> ImmutableList<T> list(T... items) {
        return ImmutableList.of(items);
    }
}
