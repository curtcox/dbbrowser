package com.cve.io.db.select;

import com.cve.log.Log;
import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBRowFilter;
import com.cve.model.db.Join;
import com.cve.model.db.DBLimit;
import com.cve.model.db.Order;
import com.cve.model.db.Select;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBValue;
import com.cve.util.URIs;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class URIRenderTest {

    Log log;

    @Test
    public void renderSelectCustomerPersonName() {
        DBServer      server = DBServer.uri(URIs.of("server"));
        Database  database = server.databaseName("customer");
        DBTable       person = database.tableName("person");
        DBColumn        name = person.columnNameType("name",String.class);
        Select      select = Select.from(database,person,name);
        URI expected = URIs.of("/*/server/customer/customer.person/name/");
        URI actual = DBURIRenderer.render(select,Search.EMPTY);
        assertEquals(expected,actual);
    }

    @Test
    public void renderSelectCustomerPersonNameAge() {
        DBServer      server = DBServer.uri(URIs.of("server"));
        Database  database = server.databaseName("customer");
        DBTable       person = database.tableName("person");
        DBColumn        name = person.columnNameType("name",String.class);
        DBColumn         age = person.columnNameType("age",Integer.class);
        Select      select = Select.from(database,person,name,age);
        URI expected = URIs.of("/*/server/customer/customer.person/name+age/");
        URI actual = DBURIRenderer.render(select,Search.EMPTY);
        assertEquals(expected,actual);
    }

    @Test
    public void renderSelectCustomerPersonNameAgeAccount() {
        DBServer      server = DBServer.uri(URIs.of("server"));
        Database  database = server.databaseName("customer");
        DBTable       person = database.tableName("person");
        DBTable      account = database.tableName("account");

        DBColumn          name = person.columnNameType("name",String.class);
        DBColumn  person_email = person.columnNameType("email",String.class);
        DBColumn           sex = person.columnNameType("sex",String.class);
        DBColumn        number = account.columnNameType("number",Integer.class);
        DBColumn account_email = account.columnNameType("email",String.class);
        Join            join = Join.of(person_email, account_email);
        DBRowFilter        filter = DBRowFilter.of(sex, DBValue.of("F"),log);
        Order          order = Order.ascending(name);
        AggregateFunction self = AggregateFunction.IDENTITY;
        Select        select = Select.from(
                list(database),list(person,account),list(name,number),list(self,self),list(join),list(filter),list(order),list(),DBLimit.DEFAULT);
        URI expected = URIs.of(
            "/*/server/customer/" + // server databases
            "customer.person+customer.account/" + // tables
            "name+0number/" + // columns
            "email=0email/" + // join
            "sex=F/" + // filter
            "name=ASC/" // order
            );
        URI actual = DBURIRenderer.render(select,Search.EMPTY);
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
