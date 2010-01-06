package com.cve.db;

import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ResultSetTest {

    public ResultSetTest() {}


    @Test
    public void selectGetterEqualsConstructor() {
        ImmutableList<Database> databases = ImmutableList.of();
        ImmutableList<DBTable>       tables = ImmutableList.of();
        ImmutableList<DBColumn>     columns = ImmutableList.of();
        ImmutableList<DBRow>           rows = ImmutableList.of();
        ImmutableMap<Cell,DBValue>   values = ImmutableMap.of();
        DBResultSet               resultSet = DBResultSet.of(databases,tables,columns,rows,values);
        assertEquals(tables, resultSet.tables);
        assertEquals(columns,resultSet.columns);
        assertEquals(rows,   resultSet.rows);
    }

    @Test
    public void equality() {
        DBServer         server = DBServer.uri(URIs.of("server"));

        Database       database = server.databaseName("INFORMATION_SCHEMA");
        DBTable            tables = database.tableName("TABLES");
        DBColumn     tableCatalog = DBColumn.tableNameType(tables,"TABLE_CATALOG",String.class);
        List<DBRow>          rows = Lists.newArrayList();
        Map<Cell,DBValue>  values = Maps.newHashMap();
        DBValue value = DBValue.of("TEST");
        for (int i=0; i<28; i++) {
            DBRow row = DBRow.number(i);
            rows.add(row);
            values.put(Cell.at(row, tableCatalog), value);
        }
        ImmutableList<DBRow> fixedRows = ImmutableList.copyOf(rows);
        ImmutableMap<Cell,DBValue> fixedValues1 = ImmutableMap.copyOf(values);
        ImmutableMap<Cell,DBValue> fixedValues2 = ImmutableMap.copyOf(values);
        DBResultSet     resultSet1 = DBResultSet.of(database,tables,tableCatalog,fixedRows,fixedValues1);
        DBResultSet     resultSet2 = DBResultSet.of(database,tables,tableCatalog,fixedRows,fixedValues2);
        assertEquals(resultSet1,resultSet2);
    }


}