package com.cve.web.db;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.Cell;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.DBRow;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBLimit;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBValue;
import com.cve.io.db.DBConnection;
import com.cve.io.db.DBConnectionFactory;
import com.cve.io.db.DBResultSetIO;
import com.cve.io.db.driver.DBDriver;
import com.cve.io.db.DBResultSetMetaData;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import static com.cve.web.db.FreeFormQueryModel.*;
import static com.cve.log.Log.args;
import static com.cve.util.Check.notNull;
/**
 * Handles "free-form" SQL select queries.
 * The queries are run against the server specified in the request URL.
 * If a database is specified, then it is used.
 * In other words, either
 * /server/select?q=...
 * or
 * /server/database/select?q=...
 * @author Curt
 */
public final class FreeFormQueryHandler extends AbstractRequestHandler {

    final DBServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    private FreeFormQueryHandler(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
    }

    public static FreeFormQueryHandler of(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new FreeFormQueryHandler(serversStore,managedFunction);
    }
    
    /**
     * Do we handle this URI?
     */
    @Override
    public boolean handles(String uri) {
        args(uri);
        return isFreeFormQueryRequest(uri);
    }

    @Override
    public FreeFormQueryModel get(PageRequest request) {
        args(request);
        ImmutableMap<String,String> params = request.parameters;
        String        query = params.get(Q);
        if (query==null) {
            query = "";
        }
        SQL sql = SQL.of(query);
        if (query.isEmpty()) {
            DBResultSet results = DBResultSet.NULL;
            DBResultSetMetaData meta = DBResultSetMetaData.NULL;
            String      message = "Type SQL select statement to be executed.";
            return page(sql,results,meta,message,null);
        }
        String uri = request.requestURI;
        DBServer server = DBURICodec.getServer(uri);
        if (isServerOnlyQuery(uri)) {
            try {
                DBConnection connection = DBConnectionFactory.getConnection(server,serversStore,managedFunction);
                ResultsAndMore results = exec(server,sql,connection);
                String      message = "Type SQL select statement to be executed.";
                return page(sql,results.resultSet,results.meta,message,null);
            } catch (SQLException e) {
                return page(sql,DBResultSet.NULL,DBResultSetMetaData.NULL,e.getMessage(),e);
            }
        }
        Database database = DBURICodec.getDatabase(uri);
        try {
            DBConnection connection = DBConnectionFactory.getConnection(server,database,serversStore,managedFunction);
            ResultsAndMore results = exec(server,sql,connection);
            String      message = "Type SQL select statement to be executed.";
            return page(sql,results.resultSet,results.meta,message,null);
        } catch (SQLException e) {
            return page(sql,DBResultSet.NULL,DBResultSetMetaData.NULL,e.getMessage(),e);
        }
    }

    static FreeFormQueryModel page(SQL sql, DBResultSet results, DBResultSetMetaData meta, String message, SQLException e) {
        args(sql,results,message,e);
        AnnotatedStackTrace trace = (e==null)
            ? AnnotatedStackTrace.NULL
            : Log.annotatedStackTrace(e);
        return new FreeFormQueryModel(sql,results,meta,message,trace);
    }

    static ResultsAndMore exec(DBServer server, SQL sql, DBConnection connection) throws SQLException {
        args(server,sql,connection);
        DBResultSetIO               results = connection.select(sql).value;
        DBResultSetMetaData            meta = connection.getMetaData(server,results);
        ImmutableList<Database>   databases = meta.databases;
        ImmutableList<DBTable>       tables = meta.tables;
        ImmutableList<DBColumn>     columns = meta.columns;
        List<DBRow>                    rows = Lists.newArrayList();
        Map<Cell,DBValue>              values = Maps.newHashMap();
        ImmutableList<AggregateFunction> functions = meta.functions;
        int cols = columns.size();
        DBLimit limit = DBLimit.DEFAULT;
        for (int r=0; r<(limit.limit - 1); r++) {
            DBRow row = DBRow.number(r);
            rows.add(row);
            r++;
            for (int c=1; c<=cols; c++) {
                Object v = results.rows.get(r).get(c);
                DBValue value = DBValue.of(v);
                values.put(Cell.at(row, columns.get(c-1),functions.get(c-1)), value);
            }
        }
        boolean more = results.rows.size() > limit.limit;
        ImmutableList<DBRow>         fixedRows = ImmutableList.copyOf(rows);
        ImmutableMap<Cell,DBValue>   fixedValues = ImmutableMap.copyOf(values);
        return new ResultsAndMore(DBResultSet.of(databases, tables, columns, fixedRows, fixedValues),meta,more);
    }

    /**
     * A result set, plus a flag to indicate if more data is available
     */
    private static class ResultsAndMore {
        final DBResultSet resultSet;
        final DBResultSetMetaData meta;
        final boolean more;
        ResultsAndMore(DBResultSet resultSet, DBResultSetMetaData meta, boolean more) {
            this.resultSet = resultSet;
            this.meta      = meta;
            this.more      = more;
        }
    }

    /**
     * Is this a query where only the server is specified in the URL?
     */
    static boolean isServerOnlyQuery(String uri) {
        return true;
    }

    /**
     * Return true if URL is of the form
     * /server/db/
     */
    static boolean isFreeFormQueryRequest(String uri) {
        if (!uri.contains("/select")) {
            return false;
        }
        if ((!uri.endsWith("/select")) && (!uri.contains("/select?"))) {
            return false;
        }
        int slashes = URIs.slashCount(uri);
        return slashes > 1;
    }

    /**
     * Return a URI that links to a free-form query page loaded with the
     * given select statement.
     */
    public URI linkTo(Select select, Search search) {
        args(select,search);
        DBServer server = select.server;
        DBConnection connection = DBConnectionFactory.getConnection(server,serversStore,managedFunction);
        DBDriver driver = connection.getInfo().driver;
        SQL sql = driver.render(select,search);
        URI  target = URIs.of("/+/" + server.uri + "/select?q=" + URLEncoder.encode(sql.toString()));
        return target;
    }
}
