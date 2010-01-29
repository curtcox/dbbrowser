package com.cve.web.db.databases;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.Database;
import com.cve.model.db.SelectResults;
import com.cve.web.db.render.ResultsTableRenderer;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.Search;
import com.cve.web.db.NavigationButtons;
import java.net.URI;

/**
 * Renders the results of searching the entire database.
 * @author curt
 */
final class DatabaseContentsSearchPageRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    private DatabaseContentsSearchPageRenderer() {
        
    }

    static DatabaseContentsSearchPageRenderer of() {
        return new DatabaseContentsSearchPageRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        DatabaseContentsSearchPage page = (DatabaseContentsSearchPage) model;
        Database database = page.database;
        Search     search = page.search;
        String     target = search.target;
        String title = "Occurences of " + target + " in " + database.name;
        NavigationButtons b = NavigationButtons.of();
        String[] navigation = new String[] {
            b.ADD_SERVER, b.REMOVE_SERVER , b.SHUTDOWN, title, b.search(search)
        };
        StringBuilder out = new StringBuilder();
        for (SelectResults results : page.resultsList) {
            out.append(ResultsTableRenderer.render(results, client));
        }
        String guts = out.toString();
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }
}
