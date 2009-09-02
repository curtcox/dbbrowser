package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.html.CSS;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.html.SimpleTooltip;
import com.cve.html.Tooltip;
import com.cve.util.URIs;
import java.net.URI;
import static com.cve.html.HTML.*;
/**
 * For picking a database server.
 */
public final class ServersPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        ServersPage page = (ServersPage) model;
        String title = "Available Servers";
        String[] navigation = new String[] { addServer(), removeServer() , shutdown() , title };
        String guts  = tableOfServers(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }

    String addServer() {
        Tooltip tip = SimpleTooltip.of("Add a database server");
        Label text = Label.of("+");
        URI target = URIs.of("add");
        URI  image = Icons.PLUS;
        return Link.textTargetTipImage(text,target,tip,image).toString();
    }

    String removeServer() {
        Tooltip tip = SimpleTooltip.of("Remove a database server");
        Label text = Label.of("-");
        URI target = URIs.of("remove");
        URI  image = Icons.MINUS;
        return Link.textTargetTipImage(text,target,tip,image).toString();
    }

    String shutdown() {
        Tooltip tip = SimpleTooltip.of("Shutdown DBBrowser");
        Label text = Label.of("X");
        URI target = URIs.of("exit");
        URI  image = Icons.OFF;
        return Link.textTargetTipImage(text,target,tip,image).toString();
    }

    String login() {
        return Link.textTarget(Label.of("login"), URIs.of("login")).toString();
    }

    String logout() {
        return Link.textTarget(Label.of("logout"), URIs.of("logout")).toString();
    }
    
    /**
     * Return true if the user is currently logged in.
     * @return
     */
    boolean loggedIn() {
        return false;
    }

    /**
     * Return a table of all the available servers.
     */
    static String tableOfServers(ServersPage page) {
        StringBuilder out = new StringBuilder();
        out.append(tr(th("Database Server") + th("Databases")));
        for (Server server : page.servers) {
            out.append(
                tr(
                    td(server.linkTo().toString(),CSS.SERVER) +
                    td(databasesOn(page,server),       CSS.DATABASE))
            );
            server.linkTo();
        }
        
        return table(out.toString());
    }

    /**
     * Return a list of all (or at least the first several) databases
     * available on the given server.
     */
    static String databasesOn(ServersPage page, Server server) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (Object object : page.databases.get(server)) {
            if (object instanceof Database) {
                Database database = (Database) object;
                out.append(database.linkTo() + " ");
            } else if (object instanceof Throwable) {
                Throwable t = (Throwable) object;
                out.append(t.getMessage());
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
