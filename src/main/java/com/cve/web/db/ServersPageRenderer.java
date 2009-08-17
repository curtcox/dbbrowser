package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.html.CSS;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.URIs;
import static com.cve.html.HTML.*;
/**
 * For picking a database server.
 */
public final class ServersPageRenderer implements ModelHtmlRenderer {

    public String render(Model model, ClientInfo client) {
        ServersPage page = (ServersPage) model;
        return render(page,client);
    }

    public String render(ServersPage page, ClientInfo client) {
        String actions = addServer() + removeServer() + 
               (loggedIn() ? logout() : login());
        return
            actions +
            h1("Available Servers") +
            tableOfServers(page)
        ;
    }

    String addServer() {
        return Link.textTarget(Label.of("+"), URIs.of("add")).toString();
    }

    String removeServer() {
        return Link.textTarget(Label.of("-"), URIs.of("remove")).toString();
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
        for (Database database : page.databases.get(server)) {
            out.append(database.linkTo() + " ");
            i++;
            if (i>20) {
                out.append("...");
                return out.toString();
            }
        }
        return out.toString();
    }

}
