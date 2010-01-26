package com.cve.web.db.servers;

import com.cve.web.*;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.db.NavigationButtons;
import com.cve.web.log.ObjectLink;
import java.net.URI;

import static com.cve.web.db.NavigationButtons.*;
import static com.cve.util.Check.notNull;

/**
 * For picking a database server.
 */
final class ServersPageRenderer implements ModelHtmlRenderer {

    private final HTMLTags tags;
    
    private final Log log;

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    private ServersPageRenderer(Log log) {
        this.log = notNull(log);
        tags = HTMLTags.of(log);
    }

    public static ServersPageRenderer of(Log log) {
        return new ServersPageRenderer(log);
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.notNullArgs(model,client);
        ServersPage page = (ServersPage) model;
        String title = "Available Servers";
        String[] navigation = getNavigation(log);
        String guts  = tableOfServers(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }

    String[] getNavigation(String title) {
        NavigationButtons b = NavigationButtons.of(log);
        return new String[] {
            b.ADD_SERVER, b.REMOVE_SERVER , b.SHUTDOWN , title, b.SEARCH
        };
    }
    
    /**
     * Return a table of all the available servers.
     */
    String tableOfServers(ServersPage page) {
        log.notNullArgs(page);
        StringBuilder out = new StringBuilder();
        out.append(tr(th("Database Server") + th("Databases")));
        for (DBServer server : page.servers) {
            out.append(
                tr(
                    td(server.linkTo().toString(),  CSS.SERVER) +
                    td(databasesOn(page,server),    CSS.DATABASE))
            );
        }
        
        return borderTable(out.toString());
    }

    /**
     * Return a list of all (or at least the first several) databases
     * available on the given server.
     */
    String databasesOn(ServersPage page, DBServer server) {
        log.notNullArgs(page,server);
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

    String h1(String s) { return tags.h1(s); }
    String h2(String s) { return tags.h2(s); }
    String tr(String s) { return tags.tr(s); }
    String td(String s) { return tags.td(s); }
String th(String s) { return tags.th(s); }
String table(String s) { return tags.table(s); }
String borderTable(String s) { return tags.borderTable(s); }

}
