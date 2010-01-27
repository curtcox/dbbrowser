package com.cve.io.db.select;

import com.cve.io.db.DBConnection;
import com.cve.model.db.Cell;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.Hints;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.DBRow;
import com.cve.model.db.Select;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.SelectContext;
import com.cve.model.db.DBValue;
import com.cve.io.db.DBConnectionFactory;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;
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

    Log log;

    @Test
    public void renderSelectPerson() throws SQLException, ClassNotFoundException {
        DBConnection connection = newMemoryDB();

        DBServer         server = DBServer.uri(URIs.of("server"),log);
        Hints           hints = Hints.NONE;

        Database       database = server.databaseName("INFORMATION_SCHEMA");
        DBTable            tables = database.tableName("TABLES");
        DBColumn     tableCatalog = DBColumn.tableNameType(tables,"TABLE_CATALOG",String.class);
        Select           select = Select.from(database,tables,tableCatalog);
        List<DBRow>          rows = Lists.newArrayList();
        Map<Cell,DBValue>  values = Maps.newHashMap();
        DBValue value = DBValue.of("UNNAMED");
        for (int i=0; i<19; i++) {
            DBRow row = DBRow.number(i);
            rows.add(row);
            values.put(Cell.at(row, tableCatalog), value);
        }
        ImmutableList<DBRow> fixedRows = ImmutableList.copyOf(rows);
        ImmutableMap<Cell,DBValue> fixedValues = ImmutableMap.copyOf(values);
        DBResultSet     resultSet = DBResultSet.of(database,tables,tableCatalog,fixedRows,fixedValues,log);
        SelectResults  expected = SelectResults.selectResultsHintsCountMore(select,resultSet,Hints.NONE,28,true,log);
        SelectContext   context = SelectContext.of(select, Search.EMPTY, server, connection, hints);
        SelectResults    actual = SelectExecutor.of(log).run(context);

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
    public DBConnection newMemoryDB()
        throws ClassNotFoundException, SQLException
    {
        JDBCURL url = JDBCURL.uri(URIs.of("jdbc:h2:mem:"));
        String user = "";
        String password = "";
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(url, user, password);
        DBServersStore serversStore = MemoryDBServersStore.of();
        ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();

        return DBConnectionFactory.of(serversStore, managedFunction, log).getConnection(info);
    }
}
