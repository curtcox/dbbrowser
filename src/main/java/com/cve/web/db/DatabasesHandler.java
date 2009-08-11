package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBMetaData;
import com.cve.util.URIParser;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.sql.SQLException;


/**
 * For picking a database.
 */
public final class DatabasesHandler extends AbstractRequestHandler {


    @Override
    public PageResponse get(PageRequest request) throws IOException, SQLException {
        String uri = request.getRequestURI();

        Server     server = URIParser.getServer(uri);
        DBMetaData  meta = DBConnection.getDbmd(server);
        ImmutableList<Database> databases = meta.getDatabasesOn(server);
        ImmutableMultimap<Database,DBTable> tables = tablesOn(databases);
        return PageResponse.of(new DatabasesPage(server,databases,tables));
    }

    @Override
    public boolean handles(String uri) {
        return isDatabaseListRequest(uri);
    }

    /**
     * Return true, if of the form
     * /server/
     */
    static boolean isDatabaseListRequest(String uri) {
        return URIParser.getServer(uri)!=null &&
               URIParser.getDatabases(uri).isEmpty();
    }

    static ImmutableMultimap<Database,DBTable> tablesOn(ImmutableList<Database> databases) throws SQLException {
        Multimap<Database,DBTable> tables = HashMultimap.create();
        for (Database database : databases) {
            DBMetaData  meta = DBConnection.getDbmd(database.getServer());
            for (DBTable table : meta.getTablesOn(database)) {
                tables.put(database,table);
            }
        }
        return ImmutableMultimap.copyOf(tables);
    }

}
