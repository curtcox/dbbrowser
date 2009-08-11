package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.dbio.DBConnection;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.dbio.DBMetaData;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.stores.ServersStore;
import com.cve.util.Throwables;
import com.cve.util.URIParser;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static com.cve.html.HTML.*;

/**
 * For showing about a server.
 */
public final class DatabaseMetaHandler extends AbstractRequestHandler {

    private static final String PREFIX = "/meta/";

    private DatabaseMetaHandler() { super("^" + PREFIX); }

    public static RequestHandler newInstance() {
        return new DatabaseMetaHandler();
    }

    public PageResponse get(PageRequest request) throws IOException {
        String uri = request.getRequestURI();

        String suffix = uri.substring(PREFIX.length() - 1);
        Server server = URIParser.getServer(suffix);
        String method = URIParser.getMetaDataMethod(suffix);
        return
            PageResponse.of(
                new StringModel(
                    h1("Available Metadata for " + server) +
                    page(server,method)))
        ;
    }

    /**
     * Return true if URL is of the form
     * /server/meta/ or
     * /server/meta/method/
     */
    static boolean isDatabaseMetaRequest(String uri) {
        return new DatabaseMetaHandler().handles(uri);
    }

    static String page(Server server, String method) {
        try {
            return tryPage(server,method);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static String tryPage(Server server, String method) throws SQLException {
        if (method.equals(""))               { return getIndex(server);       }
        if (method.equals("attributes"))     { return getAttributes(server);  }
        if (method.equals("clientInfoProperties")) { return getClientInfoProperties(server);  }
        if (method.equals("catalogs"))       { return getCatalogs(server);  }
        if (method.equals("tables"))         { return getTables(server);  }
        if (method.equals("columns"))        { return getColumns(server);  }
        if (method.equals("crossReference")) { return getCrossReference(server);  }
        if (method.equals("exportedKeys"))   { return getExportedKeys(server);  }
        if (method.equals("importedKeys"))   { return getImportedKeys(server);  }
        if (method.equals("primaryKeys"))    { return getPrimaryKeys(server);  }
        if (method.equals("indexInfos"))     { return getIndexInfos(server);  }
        if (method.equals("schemas"))        { return getSchemas(server);  }
        throw new IllegalArgumentException(method);
    }

    private static final String CROSS_REF_DESC =
       "Retrieves a description of the foreign key columns in the given foreign " +
       "key table that reference the primary key or the columns representing " +
       "a unique constraint of the parent table (could be the same or a " +
       "different table). The number of columns returned from the parent table " +
       "must match the number of columns that make up the foreign key.  " +
       "They are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and KEY_SEQ.";

    private static final String EXPORTED_KEYS_DESC =
        "Retrieves a description of the foreign key columns in the given " +
        "foreign key table that reference the primary key or the columns " +
        "representing a unique constraint of the parent table (could be " +
        "the same or a different table). The number of columns returned " +
        "from the parent table must match the number of columns that make " +
        "up the foreign key. They are ordered by " +
        "FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and KEY_SEQ.";

    private static final String IMPORTED_KEYS_DESC =
         "Retrieves a description of the primary key columns that are " +
         "referenced by the given table's foreign key columns (the primary " +
         "keys imported by a table). They are ordered by " +
         "PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, and KEY_SEQ.";

    static String getIndex(Server server) throws SQLException {
        return
        table(
            tableLinkRow("Attributes",            "attributes", "")  +
            tableLinkRow("Client Info Properties","clientInfoProperties", "")  +
            tableLinkRow("Catalogs",              "catalogs", "")  +
            tableLinkRow("Tables",                "tables",   "")   +
            tableLinkRow("Columns",               "columns", "")   +
            tableLinkRow("Cross Reference",       "crossReference", CROSS_REF_DESC)  +
            tableLinkRow("Exported Keys",         "exportedKeys",   EXPORTED_KEYS_DESC)  +
            tableLinkRow("Imported Keys",         "importedKeys",   IMPORTED_KEYS_DESC) +
            tableLinkRow("Primary Keys",          "primaryKeys", "") +
            tableLinkRow("Index Infos",           "indexInfos", "")  +
            tableLinkRow("Schemas",               "schemas", "")
        );
    }

    static String tableLinkRow(String label, String target, String description) {
         return tr(
             td(Link.textTarget(Label.of(label), URIs.of(target)).toString()) +
             td(description));
    }

    static String getAttributes(Server server) throws SQLException {
        return render(metaFor(server).getAttributes(null, null, null, null));
    }

    static String getCatalogs(Server server) throws SQLException {
        return render(metaFor(server).getCatalogs());
    }

    static String getSchemas(Server server) throws SQLException {
        return render(metaFor(server).getSchemas());
    }

    static String getTables(Server server) throws SQLException {
        String          catalog = null;
        String    schemaPattern = null;
        String tableNamePattern = null;
        String[]          types = null;
        return render(metaFor(server).getTables(catalog, schemaPattern, tableNamePattern, types));
    }

    static String getColumns(Server server) throws SQLException {
        StringBuilder out = new StringBuilder();
        for (Database database : DBConnection.getDbmd(server).getDatabasesOn(server)) {
            String catalog           = database.getName();
            String schemaPattern     = null;
            String tableNamePattern  = null;
            String columnNamePattern = null;
            try {
                out.append(render(metaFor(server).getColumns(catalog,schemaPattern,tableNamePattern,columnNamePattern)));
            } catch (SQLException e) {
                out.append(Throwables.toHtml(e));
            }
        }
        return out.toString();
    }

    static String getClientInfoProperties(Server server) throws SQLException {
        return render(metaFor(server).getClientInfoProperties());
    }

    static String getCrossReference(Server server) throws SQLException {
        String parentCatalog = null;
        String parentSchema = null;
        String parentTable = null;
        String foreignCatalog = null;
        String foreignSchema = null;
        String foreignTable = null;

        return render(metaFor(server).getCrossReference(
            parentCatalog, parentSchema, parentTable, foreignCatalog,
            foreignSchema, foreignTable));
    }

    static String getExportedKeys(Server server) throws SQLException {
        StringBuilder out = new StringBuilder();
        for (DBTable table : getTablesOn(server)) {
             String catalog = table.getDatabase().getName();
             String  schema = null;
             out.append(render(metaFor(server).getExportedKeys(catalog, schema, table.getName())));
        }
        return out.toString();
    }

    static ImmutableList<DBTable> getTablesOn(Server server) throws SQLException {
        List<DBTable> list = Lists.newArrayList();
        DBMetaData dbmd = DBConnection.getDbmd(server);
        for (Database database : dbmd.getDatabasesOn(server)) {
            for (DBTable table : dbmd.getTablesOn(database)) {
                list.add(table);
            }
        }
        return ImmutableList.copyOf(list);
    }

    static String getImportedKeys(Server server) throws SQLException {
        StringBuilder out = new StringBuilder();
        for (DBTable table : getTablesOn(server)) {
             String catalog = table.getDatabase().getName();
             String  schema = null;
             out.append(render(metaFor(server).getImportedKeys(catalog, schema, table.getName())));
        }
        return out.toString();
    }

    static String getPrimaryKeys(Server server) throws SQLException {
        StringBuilder out = new StringBuilder();
        DatabaseMetaData meta = metaFor(server);
        for (DBTable table : getTablesOn(server)) {
             String tableName = table.getName();
             String catalog = table.getDatabase().getName();
             String  schema = null;
             out.append(render(meta.getPrimaryKeys(catalog, schema, tableName)));
        }
        return out.toString();
    }

    static String getIndexInfos(Server server) throws SQLException {
        return render(metaFor(server).getIndexInfo(null, null, null, true, true));
    }

    static DatabaseMetaData metaFor(Server server) throws SQLException {
        DBConnection connection = ServersStore.getConnection(server);
        return connection.getJDBCMetaData();
    }

    public static String render(ResultSet results) throws SQLException {
        try {
            StringBuilder      out = new StringBuilder();
            ResultSetMetaData rsmd = results.getMetaData();
            int cols = rsmd.getColumnCount();
            out.append("<tr>");
            for (int i=1; i<=cols; i++) {
                out.append(th("" + rsmd.getColumnName(i)));
            }
//            out.append("<tr></tr>\r");
//            for (int i=1; i<=cols; i++) {
//                out.append(th("" + rsmd.getColumnLabel(i)));
//            }
//            out.append("<tr></tr>\r");
//            for (int i=1; i<=cols; i++) {
//                out.append(th("" + rsmd.getColumnTypeName(i)));
//            }
            out.append("</tr>\r");
            String header = out.toString();
            out = new StringBuilder();
            out.append(header);
            while (results.next()) {
                out.append("<tr>");
                for (int i=1; i<=cols; i++) {
                    out.append(td("" + results.getObject(i)));
                }
                out.append("</tr>\r");
            }
            return table(out.toString());
        } finally {
            results.close();
        }
    }


}
