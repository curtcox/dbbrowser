package com.cve.web.db.databases;

import com.cve.lang.URIObject;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.Database;
import com.cve.model.db.SelectResults;
import com.cve.ui.UIElement;
import com.cve.web.db.render.ResultsTableRenderer;
import com.cve.util.URIs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.Search;
import com.cve.web.db.NavigationButtons;


/**
 * Renders the results of searching the entire database.
 * @author curt
 */
final class DatabaseContentsSearchPageRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private static URIObject HELP = URIs.of("/resource/help/Servers.html");

    private DatabaseContentsSearchPageRenderer() {
        
    }

    static DatabaseContentsSearchPageRenderer of() {
        return new DatabaseContentsSearchPageRenderer();
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
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
