package com.cve.db.dbio;

import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.DBRowFilter;
import com.cve.db.Join;
import com.cve.db.DBLimit;
import com.cve.db.Order;
import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.db.DBServer;
import com.cve.db.DBTable;
import com.cve.db.DBValue;
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

    @Test
    public void renderSelectPerson() {
        DBServer      server = DBServer.uri(URIs.of("server"));
        Database  database = server.databaseName("customer");
        DBTable       person = database.tableName("person");
        DBColumn        name = person.columnNameType("name",String.class);
        DBRowFilter      filter = DBRowFilter.of(name, DBValue.of("Smith"));
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
        DBRowFilter        filter = DBRowFilter.of(name, DBValue.of("Smith"));
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
