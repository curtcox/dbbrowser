package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.web.*;
import com.cve.db.Server;
import com.cve.html.CSS;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.log.ObjectLink;
import java.net.URI;
import static com.cve.html.HTML.*;
import static com.cve.web.db.NavigationButtons.*;

/**
 * For finding stuff in a database server.
 */
public final class ServersSearchPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        ServersSearchPage page = (ServersSearchPage) model;
        String title = "Available Servers";
        String[] navigation = new String[] {
            ADD_SERVER, REMOVE_SERVER , SHUTDOWN, title, search(page.search.target)
        };
        String guts  = tableOfMatches(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }

    
    /**
     * Return a table of all the available servers.
     */
    static String tableOfMatches(ServersSearchPage page) {
        StringBuilder out = new StringBuilder();
        out.append(tr(th("Database Server") + th("Database")));
        for (Server server : page.servers) {
            out.append(
                tr(
                    td(server.linkTo().toString(),CSS.SERVER) +
                    td(columnsOn(page,server),    CSS.DATABASE))
            );
            server.linkTo();
        }
        
        return borderTable(out.toString());
    }

    /**
     * Return a list of all (or at least the first several) databases
     * available on the given server.
     */
    static String columnsOn(ServersSearchPage page, Server server) {
        StringBuilder out = new StringBuilder();
        for (Object object : page.columns.get(server)) {
            if (object instanceof DBColumn) {
                DBColumn column = (DBColumn) object;
                out.append(column.linkTo() + " ");
            } else if (object instanceof AnnotatedStackTrace) {
                AnnotatedStackTrace t = (AnnotatedStackTrace) object;
                String message = t.throwable.getMessage();
                out.append(ObjectLink.to(message, t));
            } else {
                throw new IllegalArgumentException("" + object);
            }
        }
        return out.toString();
    }

}
