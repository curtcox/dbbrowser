package com.cve.db.select;

import com.cve.db.dbio.DBConnection;
import com.cve.db.Cell;
import com.cve.db.ConnectionInfo;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Hints;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.Select;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.JDBCURL;
import com.cve.db.SelectContext;
import com.cve.db.Value;
import com.cve.util.URIs;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ResultsRenderTest {

    @Test
    public void renderSelectPerson() throws SQLException, ClassNotFoundException {
        DBConnection connection = newMemoryDB();

        Server         server = Server.uri(URIs.of("server"));
        Hints           hints = Hints.NONE;

        Database       database = server.databaseName("INFORMATION_SCHEMA");
        DBTable            tables = database.tableName("TABLES");
        DBColumn     tableCatalog = DBColumn.tableNameType(tables,"TABLE_CATALOG",String.class);
        Select           select = Select.from(database,tables,tableCatalog);
        List<DBRow>          rows = Lists.newArrayList();
        Map<Cell,Value>  values = Maps.newHashMap();
        Value value = Value.of("UNNAMED");
        for (int i=0; i<19; i++) {
            DBRow row = DBRow.number(i);
            rows.add(row);
            values.put(Cell.at(row, tableCatalog), value);
        }
        ImmutableList<DBRow> fixedRows = ImmutableList.copyOf(rows);
        ImmutableMap<Cell,Value> fixedValues = ImmutableMap.copyOf(values);
        DBResultSet     resultSet = DBResultSet.of(database,tables,tableCatalog,fixedRows,fixedValues);
        SelectResults  expected = SelectResults.selectResultsHintsCountMore(select,resultSet,Hints.NONE,28,true);
        SelectContext   context = SelectContext.of(select, Search.EMPTY, server, connection, hints);
        SelectResults    actual = SelectExecutor.run(context);

        assertEquals(expected.server          ,actual.server);
        assertEquals(expected.count           ,actual.count);
        assertEquals(expected.type            ,actual.type);
        assertEquals(expected.hints           ,actual.hints);
        assertEquals(expected.select          ,actual.select);
        assertEquals(expected.resultSet.rows  ,actual.resultSet.rows);
        assertEquals(expected.resultSet.values,actual.resultSet.values);
        assertEquals(expected.resultSet       ,actual.resultSet);
        assertEquals(expected                 ,actual);
    }

     /**
     * Create a new database.
     */
    public static DBConnection newMemoryDB()
        throws ClassNotFoundException, SQLException
    {
        JDBCURL url = JDBCURL.uri(URIs.of("jdbc:h2:mem:"));
        String user = "";
        String password = "";
        ConnectionInfo info = ConnectionInfo.urlUserPassword(url, user, password);
        return DBConnection.info(info);
    }
}
