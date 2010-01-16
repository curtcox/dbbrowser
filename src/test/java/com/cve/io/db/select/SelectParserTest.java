package com.cve.io.db.select;

import com.cve.io.db.select.SelectParser;
import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBResultSetMetaData;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class SelectParserTest {

    @Test(expected=IllegalArgumentException.class)
    public void emptyMetaIsIllegalArgument() {
        SQL sql = SQL.of("");
        List<Database>          databases = Lists.newArrayList();
        List<DBTable>              tables = Lists.newArrayList();
        List<DBColumn>            columns = Lists.newArrayList();
        List<AggregateFunction> functions = Lists.newArrayList();
        DBResultSetMetaData meta = DBResultSetMetaData.of(databases,tables,columns,functions);
        SelectParser.parse(sql,meta);
    }

    @Test
    public void oneDatabaseOneTableOneColumn() {
        SQL sql = SQL.of("");
        List<Database>          databases = Lists.newArrayList();
        List<DBTable>              tables = Lists.newArrayList();
        List<DBColumn>            columns = Lists.newArrayList();
        List<AggregateFunction> functions = Lists.newArrayList();
        Database database = DBServer.uri(URIs.of("server")).databaseName("database");
        databases.add(database);
        DBTable table = database.tableName("table");
        tables.add(table);
        DBColumn column = table.columnName("column");
        columns.add(column);
        AggregateFunction function = AggregateFunction.IDENTITY;
        functions.add(function);
        DBResultSetMetaData meta = DBResultSetMetaData.of(databases,tables,columns,functions);
        Select select = SelectParser.parse(sql,meta);
        
        assertEquals(ImmutableList.copyOf(databases),select.databases);
        assertEquals(ImmutableList.copyOf(tables),select.tables);
        assertEquals(ImmutableList.copyOf(columns),select.columns);
        assertEquals(ImmutableList.copyOf(functions),select.functions);
    }

}
