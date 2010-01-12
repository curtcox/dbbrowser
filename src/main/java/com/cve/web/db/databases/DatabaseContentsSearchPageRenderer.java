package com.cve.web.db.databases;

import com.cve.model.db.Database;
import com.cve.model.db.SelectResults;
import com.cve.web.db.render.ResultsTableRenderer;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.Search;
import java.net.URI;
import static com.cve.web.db.NavigationButtons.*;
import static com.cve.log.Log.args;

/**
 * Renders the results of searching the entire database.
 * @author curt
 */
final class DatabaseContentsSearchPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        args(model,client);
        DatabaseContentsSearchPage page = (DatabaseContentsSearchPage) model;
        Database database = page.database;
        Search     search = page.search;
        String     target = search.target;
        String title = "Occurences of " + target + " in " + database.name;
        String[] navigation = new String[] {
            ADD_SERVER, REMOVE_SERVER , SHUTDOWN, title, search(search)
        };
        StringBuilder out = new StringBuilder();
        for (SelectResults results : page.resultsList) {
            out.append(ResultsTableRenderer.render(results, client));
        }
        String guts = out.toString();
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }
}
