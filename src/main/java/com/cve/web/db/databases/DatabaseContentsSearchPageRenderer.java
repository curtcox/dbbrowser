package com.cve.web.db.databases;

import com.cve.log.Log;
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
import static com.cve.util.Check.notNull;

/**
 * Renders the results of searching the entire database.
 * @author curt
 */
final class DatabaseContentsSearchPageRenderer implements ModelHtmlRenderer {

    final Log log;

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    private DatabaseContentsSearchPageRenderer(Log log) {
        this.log = notNull(log);
    }

    static DatabaseContentsSearchPageRenderer of(Log log) {
        return new DatabaseContentsSearchPageRenderer(log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.notNullArgs(model,client);
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
            out.append(ResultsTableRenderer.render(results, client,log));
        }
        String guts = out.toString();
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }
}
