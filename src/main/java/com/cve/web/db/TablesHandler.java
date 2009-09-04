package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBMetaData;
import com.cve.util.URIs;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.sql.SQLException;

import static com.cve.log.Log.args;
/**
 * For picking a table.
 */
public final class TablesHandler extends AbstractRequestHandler {

    /**
     * Do we handle this URI?
     */
    @Override
    public boolean handles(String uri) {
        return isTablesOnlyRequest(uri);
    }

    /**
     * Get the response (list of tables) for this request.
     */
    @Override
    public TablesPage get(PageRequest request) throws IOException, SQLException {
        args(request);
        String                    uri = request.requestURI;
        Server                 server = DBURIParser.getServer(uri);
        Database             database = DBURIParser.getDatabase(uri);
        DBMetaData               meta = DBConnection.getDbmd(server);
        ImmutableList<DBTable> tables = meta.getTablesOn(database);
        ImmutableMultimap<DBTable,DBColumn> columns = columnsFor(tables);
        return new TablesPage(server,database,tables,columns);
    }

    /**
     * Return true if URL is of the form
     * //server/db/
     */
    public static boolean isTablesOnlyRequest(String uri) {
        return URIs.slashCount(uri)==4;
    }

    /**
     * Return a map from the given tables to the columns they contain.
     */
    static ImmutableMultimap<DBTable,DBColumn> columnsFor(ImmutableList<DBTable> tables) throws SQLException {
        Multimap<DBTable,DBColumn> columns = HashMultimap.create();
        for (DBTable table : tables) {
            Server      server = table.database.server;
            DBMetaData    meta = DBConnection.getDbmd(server);
            for (DBColumn column : meta.getColumnsFor(table)) {
                columns.put(table, column);
            }
        }
        return ImmutableMultimap.copyOf(columns);
    }
}
