package com.cve.web.db.servers;

import com.cve.web.*;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.html.CSS;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.log.ObjectLink;
import java.net.URI;
import static com.cve.html.HTML.*;

import static com.cve.web.db.NavigationButtons.*;

/**
 * For picking a database server.
 */
final class ServersPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        ServersPage page = (ServersPage) model;
        String title = "Available Servers";
        String[] navigation = new String[] {
            ADD_SERVER, REMOVE_SERVER , SHUTDOWN , title, SEARCH
        };
        String guts  = tableOfServers(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }

    
    /**
     * Return a table of all the available servers.
     */
    static String tableOfServers(ServersPage page) {
        StringBuilder out = new StringBuilder();
        out.append(tr(th("Database Server") + th("Databases")));
        for (DBServer server : page.servers) {
            out.append(
                tr(
                    td(server.linkTo().toString(),CSS.SERVER) +
                    td(databasesOn(page,server),       CSS.DATABASE))
            );
            server.linkTo();
        }
        
        return borderTable(out.toString());
    }

    /**
     * Return a list of all (or at least the first several) databases
     * available on the given server.
     */
    static String databasesOn(ServersPage page, DBServer server) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (Object object : page.databases.get(server)) {
            if (object instanceof Database) {
                Database database = (Database) object;
                out.append(database.linkTo() + " ");
            } else if (object instanceof AnnotatedStackTrace) {
                AnnotatedStackTrace t = (AnnotatedStackTrace) object;
                String message = t.throwable.getMessage();
                out.append(ObjectLink.to(message, t));
            } else {
                throw new IllegalArgumentException("" + object);
            }
            i++;
            if (i>20) {
                out.append("...");
                return out.toString();
            }
        }
        return out.toString();
    }

}
