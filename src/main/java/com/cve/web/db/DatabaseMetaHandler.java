package com.cve.web.db;

import com.cve.html.HTMLTags;
import com.cve.web.*;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.io.db.DBConnectionFactory;
import com.cve.io.db.DBMetaData;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.Throwables;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static com.cve.util.Check.notNull;

/**
 * For showing about a server.
 */
public final class DatabaseMetaHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final ManagedFunction.Factory managedFunction;

    final DBServersStore serversStore;

    final Log log = Logs.of();

    
    final DBURICodec codec;

    final DBConnectionFactory connections;

    final HTMLTags tags;
    String h1(String s) { return tags.h1(s); }
    String h2(String s) { return tags.h2(s); }
    String tr(String s) { return tags.tr(s); }
    String td(String s) { return tags.td(s); }
    String th(String s) { return tags.th(s); }
    String table(String s) { return tags.table(s); }
    String borderTable(String s) { return tags.borderTable(s); }

    private static final String PREFIX = "/meta/";

    private DatabaseMetaHandler(
        DBMetaData.Factory db, DBServersStore serversStore, ManagedFunction.Factory managedFunction)
    {
        super("^" + PREFIX);
        this.db = notNull(db);
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
        
        codec = DBURICodec.of();
        connections = DBConnectionFactory.of(serversStore, managedFunction);
        tags = HTMLTags.of();
    }

    public static DatabaseMetaHandler of(
        DBMetaData.Factory db, DBServersStore serversStore, ManagedFunction.Factory managedFunction)
    {
        return new DatabaseMetaHandler(db,serversStore,managedFunction);
    }

    @Override
    public StringModel get(PageRequest request) {
        String uri = request.requestURI;

        String suffix = uri.substring(PREFIX.length() - 1);
        DBServer server = codec.getServer(suffix);
        String method = codec.getMetaDataMethod(suffix);
        return
            new StringModel(
                h1("Available Metadata for " + server) +
                page(server,method))
            ;
    }

    /**
     * Return true if URL is of the form
     * /server/meta/ or
     * /server/meta/method/
     */
    boolean isDatabaseMetaRequest(String uri) {
        return handles(uri);
    }

    String page(DBServer server, String method) {
        try {
            return tryPage(server,method);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    String tryPage(DBServer server, String method) throws SQLException {
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

    String getIndex(DBServer server) throws SQLException {
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

    String tableLinkRow(String label, String target, String description) {
         return tr(
             td( Link.textTarget(Label.of(label), URIs.of(target)).toString() ) +
             td(description));
    }

    String getAttributes(DBServer server) throws SQLException {
        return render(metaFor(server).getAttributes(null, null, null, null));
    }

    String getCatalogs(DBServer server) throws SQLException {
        return render(metaFor(server).getCatalogs());
    }

    String getSchemas(DBServer server) throws SQLException {
        return render(metaFor(server).getSchemas());
    }

    String getTables(DBServer server) throws SQLException {
        String          catalog = null;
        String    schemaPattern = null;
        String tableNamePattern = null;
        String[]          types = null;
        return render(metaFor(server).getTables(catalog, schemaPattern, tableNamePattern, types));
    }

    String getColumns(DBServer server) throws SQLException {
        StringBuilder out = new StringBuilder();
        for (Database database : db.of(server).getDatabasesOn(server).value) {
            String catalog           = database.name;
            String schemaPattern     = null;
            String tableNamePattern  = null;
            String columnNamePattern = null;
            try {
                out.append(render(metaFor(server).getColumns(catalog,schemaPattern,tableNamePattern,columnNamePattern)));
            } catch (SQLException e) {
                out.append(Throwables.of().toHtml(e));
            }
        }
        return out.toString();
    }

    String getClientInfoProperties(DBServer server) throws SQLException {
        return render(metaFor(server).getClientInfoProperties());
    }

    String getCrossReference(DBServer server) throws SQLException {
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

    String getExportedKeys(DBServer server) throws SQLException {
        StringBuilder out = new StringBuilder();
        for (DBTable table : getTablesOn(server)) {
             String catalog = table.database.name;
             String  schema = null;
             out.append(render(metaFor(server).getExportedKeys(catalog, schema, table.name)));
        }
        return out.toString();
    }

    ImmutableList<DBTable> getTablesOn(DBServer server) throws SQLException {
        List<DBTable> list = Lists.newArrayList();
        DBMetaData dbmd = db.of(server);
        for (Database database : dbmd.getDatabasesOn(server).value) {
            for (DBTable table : dbmd.getTablesOn(database).value) {
                list.add(table);
            }
        }
        return ImmutableList.copyOf(list);
    }

    String getImportedKeys(DBServer server) throws SQLException {
        StringBuilder out = new StringBuilder();
        for (DBTable table : getTablesOn(server)) {
             String catalog = table.database.name;
             String  schema = null;
             out.append(render(metaFor(server).getImportedKeys(catalog, schema, table.name)));
        }
        return out.toString();
    }

    String getPrimaryKeys(DBServer server) throws SQLException {
        StringBuilder out = new StringBuilder();
        DatabaseMetaData meta = metaFor(server);
        for (DBTable table : getTablesOn(server)) {
             String tableName = table.name;
             String catalog = table.database.name;
             String  schema = null;
             out.append(render(meta.getPrimaryKeys(catalog, schema, tableName)));
        }
        return out.toString();
    }

    String getIndexInfos(DBServer server) throws SQLException {
        String catalog = null;
        String schema = null;
        String table = null;
        boolean unique = true;
        boolean approximate = true;
        return render(metaFor(server).getIndexInfo(catalog, schema, table, unique, approximate));
    }

    DatabaseMetaData metaFor(DBServer server) throws SQLException {
        return connections.metaFor(server,serversStore,managedFunction);
    }

    public String render(ResultSet results) throws SQLException {
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
            return borderTable(out.toString());
        } finally {
            results.close();
        }
    }


}
