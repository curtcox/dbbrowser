package com.cve.web.db.servers;

import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.html.CSS;
import com.cve.log.Log;
import com.cve.log.Logs;

import com.cve.lang.AnnotatedStackTrace;
import com.cve.lang.URIObject;
import com.cve.ui.UIElement;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableRow;
import com.cve.ui.UITableBuilder;
import com.cve.ui.UITableCell;
import com.cve.ui.UITableHeader;
import com.cve.util.URIs;
import com.cve.web.db.NavigationButtons;
import com.cve.web.management.ObjectLink;
import com.cve.web.management.ObjectLinks;



/**
 * For picking a database server.
 */
final class ServersPageRenderer implements ModelHtmlRenderer {

    
    private final Log log = Logs.of();

    private static URIObject HELP = URIs.of("/resource/help/Servers.html");

    UITableRow    row(UITableCell... details) { return UITableRow.of(details); }
    UITableDetail detail(String s) { return UITableDetail.of(s); }
    UITableDetail detail(String s, CSS css) { return UITableDetail.of(s,css); }
    UITableHeader header(String s) { return UITableHeader.of(s); }

   /**
    * Use the factory
    */
    private ServersPageRenderer() {}

    public static ServersPageRenderer of() {
        return new ServersPageRenderer();
    }
    
    @Override
    public UIElement render(Model model, ClientInfo client) {
        log.args(model,client);
        ServersPage page = (ServersPage) model;
        String title = "Available Servers";
        String[] navigation = getNavigation(title);
        String guts  = tableOfServers(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }

    String[] getNavigation(String title) {
        log.args(title);
        NavigationButtons b = NavigationButtons.of();
        return new String[] {
            b.ADD_SERVER, b.REMOVE_SERVER , b.SHUTDOWN , title, b.SEARCH
        };
    }
    
    /**
     * Return a table of all the available servers.
     */
    String tableOfServers(ServersPage page) {
        log.args(page);
        UITableBuilder out = UITableBuilder.of();
        out.add(row(header("Database Server"),header("Databases")));
        for (DBServer server : page.servers) {
            out.add(
                row(
                    detail(server.linkTo().toString(),  CSS.SERVER),
                    detail(databasesOn(page,server),    CSS.DATABASE))
            );
        }
        
        return out.build().toString();
    }

    /**
     * Return a list of all (or at least the first several) databases
     * available on the given server.
     */
    String databasesOn(ServersPage page, DBServer server) {
        log.args(page,server);
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (Object object : page.databases.get(server)) {
            if (object instanceof Database) {
                Database database = (Database) object;
                out.append(database.linkTo() + " ");
            } else if (object instanceof AnnotatedStackTrace) {
                AnnotatedStackTrace t = (AnnotatedStackTrace) object;
                String message = t.throwable.getMessage();
                out.append(ObjectLinks.of().to(message, t));
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
