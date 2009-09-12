package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBMetaData;
import com.cve.util.URIs;
import com.cve.web.Search.Space;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Set;
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
    public Model get(PageRequest request) throws IOException, SQLException {
        args(request);
        String                    uri = request.requestURI;
        Search                 search = DBURICodec.getSearch(uri);
        Server                 server = DBURICodec.getServer(uri);
        Database             database = DBURICodec.getDatabase(uri);
        if (search.isEmpty()) {
            DBMetaData               meta = DBConnection.getDbmd(server);
            ImmutableList<DBTable> tables = meta.getTablesOn(database);
            ImmutableMultimap<DBTable,DBColumn> columns = columnsFor(tables);
            return new TablesPage(server,database,tables,columns);
        }
        if (search.space==Space.CONTENTS) {
            return DatabaseContentsSearchPageCreator.create(database,search);
        }
        return newNamesSearchPage(database,search);
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

    /**
     * Perform the requested search of the table, column, database
     * and server names.  Return a results page.
     */
    static TablesSearchPage newNamesSearchPage(Database database,Search search) throws SQLException {
        args(database,search);
        DBMetaData               meta = DBConnection.getDbmd(database.server);
        ImmutableList<DBColumn> columns = meta.getColumnsFor(database);
        Set<DBTable> filteredTables = Sets.newHashSet();
        Multimap<DBTable,DBColumn> filteredColumns = HashMultimap.create();
        for (DBColumn column : columns) {
            String     target = search.target;
            DBTable     table = column.table;
            if (isMatch(column.name,target)) {
                filteredTables.add(table);
                filteredColumns.put(table,column);
            }
            if (isMatch(table.name,target)) {
                filteredTables.add(table);
            }
        }
        return new TablesSearchPage(search, database,
            new ArrayList(filteredTables),filteredColumns);
    }

    /**
     * Return true if the name search should consider this a match.
     */
    static boolean isMatch(String text, String target) {
        return text.toUpperCase().contains(target.toUpperCase());
    }

}
