package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.dbio.DBMetaData;
import com.cve.util.URIs;
import com.cve.web.Search.Space;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import static com.cve.log.Log.args;
/**
 * For picking a table.
 */
public final class TablesHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    private TablesHandler(DBMetaData.Factory db) {
        this.db = db;
    }

    static TablesHandler of(DBMetaData.Factory db) {
        return new TablesHandler(db);
    }
    
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
            DBMetaData               meta = db.of(server);
            ImmutableList<DBTable> tables = meta.getTablesOn(database).value;
            ImmutableMultimap<DBTable,DBColumn> columns = columnsFor(tables);
            ImmutableMap<DBTable,Long>             rows = rowsFor(tables);
            return new TablesPage(server,database,tables,rows,columns);
        }
        if (search.space==Space.CONTENTS) {
            return DatabaseContentsSearchPageCreator.of(db).create(database,search);
        }
        return newNamesSearchPage(database,search);
    }

    /**
     * Return true if URL is of the form
     * /+/server/db/
     */
    public static boolean isTablesOnlyRequest(String uri) {
        return URIs.slashCount(uri)==4;
    }

    /**
     * Return a map from the given tables to the columns they contain.
     */
    ImmutableMultimap<DBTable,DBColumn> columnsFor(ImmutableList<DBTable> tables) throws SQLException {
        Multimap<DBTable,DBColumn> columns = HashMultimap.create();
        for (DBTable table : tables) {
            Server      server = table.database.server;
            DBMetaData    meta = db.of(server);
            for (DBColumn column : meta.getColumnsFor(table).value) {
                columns.put(table, column);
            }
        }
        return ImmutableMultimap.copyOf(columns);
    }

        /**
     * Return a map from the given tables to the columns they contain.
     */
    ImmutableMap<DBTable,Long> rowsFor(ImmutableList<DBTable> tables) throws SQLException {
        Map<DBTable,Long> rows = Maps.newHashMap();
        for (DBTable table : tables) {
            Server      server = table.database.server;
            DBMetaData    meta = db.of(server);
            rows.put(table, meta.getRowCountFor(table).value);
        }
        return ImmutableMap.copyOf(rows);
    }

    /**
     * Perform the requested search of the table, column, database
     * and server names.  Return a results page.
     */
    TablesSearchPage newNamesSearchPage(Database database,Search search) throws SQLException {
        args(database,search);
        DBMetaData               meta = db.of(database.server);
        ImmutableList<DBColumn> columns = meta.getColumnsFor(database).value;
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
