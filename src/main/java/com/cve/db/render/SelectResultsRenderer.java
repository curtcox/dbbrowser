package com.cve.db.render;

import com.cve.db.SelectResults;
import com.cve.db.SelectResults.Type;
import com.cve.db.Server;
import com.cve.util.Replace;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
/**
 * Renders the results of a database select as HTML.
 * The parts of this are:
 * <ol>
 * <li> a table of the data itself
 * <li> paging links
 * <li> a table of hidden data to show
 * <li> alternate diplay links
 * </ol>
 */
@Immutable
public final class SelectResultsRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/SelectResults.html");

    public SelectResultsRenderer() {}

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        SelectResults results = (SelectResults) model;
        if (results.type==Type.COLUMN_VALUE_DISTRIBUTION) {
            String guts = renderColumnValueDistributionPage(results,client);
            String title = "Values in " + results.select.columns.get(0);
            String[] nav = new String[] {};
            return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
        } else {
            String guts = renderSelectBuilderPage(results,client);
            Server server = results.server;
            String title = "Data from server " + server.toString();
            String[] nav = new String[] {
                Replace.bracketQuote("Data from <a href=[/]>server</a> /" + server.linkTo())
            };
            return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
        }
    }

    String renderColumnValueDistributionPage(SelectResults results, ClientInfo client) {
        return
            DistributionResultsTableRenderer.render(results,client) +
            PagingLinksRenderer.render(results) +
            AlternateDisplayLinksRenderer.render(results)
        ;
    }

    String renderSelectBuilderPage(SelectResults results, ClientInfo client) {
        return
            ResultsTableRenderer.render(results,client) +
            PagingLinksRenderer.render(results) +
            ShowTableRenderer.render(results) +
            AlternateDisplayLinksRenderer.render(results)
        ;
    }

}
