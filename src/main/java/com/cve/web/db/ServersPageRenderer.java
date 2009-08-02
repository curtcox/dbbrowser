package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.html.CSS;

import static com.cve.html.HTML.*;
/**
 * For picking a database server.
 */
public final class ServersPageRenderer implements ModelRenderer {

    public Object render(Model model, ClientInfo client) {
        ServersPage page = (ServersPage) model;
        return render(page,client);
    }

    public String render(ServersPage page, ClientInfo client) {
        return 
            h1("Available Servers") +
            tableOfServers(page)
        ;
    }

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
