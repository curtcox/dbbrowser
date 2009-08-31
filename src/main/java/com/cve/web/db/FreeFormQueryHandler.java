package com.cve.web.db;

import com.cve.db.AggregateFunction;
import com.cve.db.Cell;
import com.cve.db.DBColumn;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Limit;
import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.db.Server;
import com.cve.db.Value;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBDriver;
import com.cve.db.dbio.DBResultSetMetaData;
import com.cve.db.select.SelectRenderers;
import com.cve.log.Log;
import com.cve.stores.ServersStore;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIParser;
import com.cve.util.URIs;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import static com.cve.web.db.FreeFormQueryModel.*;
import static com.cve.log.Log.args;

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

    FreeFormQueryHandler() {}

    /**
     * Do we handle this URI?
     */
    @Override
    public boolean handles(String uri) {
        args(uri);
        return isFreeFormQueryRequest(uri);
    }

    @Override
    public PageResponse get(PageRequest request) throws IOException {
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
        Server server = URIParser.getServer(uri);
        if (isServerOnlyQuery(uri)) {
            try {
                DBConnection connection = ServersStore.getConnection(server);
                ResultsAndMore results = exec(server,sql,connection);
                String      message = "Type SQL select statement to be executed.";
                return page(sql,results.resultSet,results.meta,message,null);
            } catch (SQLException e) {
                return page(sql,DBResultSet.NULL,DBResultSetMetaData.NULL,e.getMessage(),e);
            }
        }
        Database database = URIParser.getDatabase(uri);
        try {
            DBConnection connection = ServersStore.getConnection(server,database);
            ResultsAndMore results = exec(server,sql,connection);
            String      message = "Type SQL select statement to be executed.";
            return page(sql,results.resultSet,results.meta,message,null);
        } catch (SQLException e) {
            return page(sql,DBResultSet.NULL,DBResultSetMetaData.NULL,e.getMessage(),e);
        }
    }

    static PageResponse page(SQL sql, DBResultSet results, DBResultSetMetaData meta, String message, SQLException e) {
        args(sql,results,message,e);
        AnnotatedStackTrace trace = (e==null)
            ? AnnotatedStackTrace.NULL
            : Log.annotatedStackTrace(e);
        return PageResponse.of(new FreeFormQueryModel(sql,results,meta,message,trace));
    }

    static ResultsAndMore exec(Server server, SQL sql, DBConnection connection) throws SQLException {
        args(server,sql,connection);
        ResultSet                   results = connection.select(sql);
        DBResultSetMetaData            meta = connection.getMetaData(server,results);
        ImmutableList<Database>   databases = meta.databases;
        ImmutableList<DBTable>       tables = meta.tables;
        ImmutableList<DBColumn>     columns = meta.columns;
        List<DBRow>                    rows = Lists.newArrayList();
        Map<Cell,Value>              values = Maps.newHashMap();
        ImmutableList<AggregateFunction> functions = meta.functions;
        int cols = columns.size();
        int    r = 0;
        Limit limit = Limit.DEFAULT;
        while (results.next() && r<(limit.limit - 1)) {
            DBRow row = DBRow.number(r);
            rows.add(row);
            r++;
            for (int c=1; c<=cols; c++) {
                Object v = getObject(results,c);
                Value value = Value.of(v);
                values.put(Cell.at(row, columns.get(c-1),functions.get(c-1)), value);
            }
        }
        boolean more = results.next();
        ImmutableList<DBRow>         fixedRows = ImmutableList.copyOf(rows);
        ImmutableMap<Cell,Value>   fixedValues = ImmutableMap.copyOf(values);
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
     * Get the object from the speciifed column.
     * Return a string describing the conversion error if one is encountered.
     */
    static Object getObject(ResultSet results, int c) throws SQLException {
        args(results,c);
        try {
            return results.getObject(c);
        } catch (SQLException e) {
            ResultSetMetaData meta = results.getMetaData();
            String        typeName = meta.getColumnTypeName(c);
            String       className = meta.getColumnClassName(c);
            return "Error converting " + typeName + "/" + className;
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
    public static URI linkTo(Select select) {
        args(select);
        Server server = select.databases.get(0).server;
        DBConnection connection = ServersStore.getConnection(server);
        DBDriver driver = connection.info.driver;
        SQL sql = SelectRenderers.render(select, driver);
        URI  target = URIs.of("/" + server.uri + "/select?q=" + URLEncoder.encode(sql.toString()));
        return target;
    }
}
