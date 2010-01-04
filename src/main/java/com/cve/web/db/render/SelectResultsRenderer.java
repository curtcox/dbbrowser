package com.cve.web.db.render;

import com.cve.db.SelectResults;
import com.cve.db.SelectResults.Type;
import com.cve.db.Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.ServersStore;
import com.cve.util.Replace;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.web.db.NavigationButtons.*;
import static com.cve.log.Log.args;
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

    final ServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    private static URI HELP = URIs.of("/resource/help/SelectResults.html");

    private SelectResultsRenderer(ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = serversStore;
        this.managedFunction = managedFunction;
    }

    public static SelectResultsRenderer of(ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new SelectResultsRenderer(serversStore,managedFunction);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        args(model,client);
        SelectResults results = (SelectResults) model;
        if (results.type==Type.COLUMN_VALUE_DISTRIBUTION) {
            return renderColumnValueDistribution(results,client);
        } else {
            return renderNormalResults(results,client);
        }
    }

    public HtmlPage renderColumnValueDistribution(SelectResults results, ClientInfo client) {
        String guts = renderColumnValueDistributionPage(results,client);
        String title = "Values in " + results.select.columns.get(0);
        String[] nav = new String[] { search(results.search) };
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    public HtmlPage renderNormalResults(SelectResults results, ClientInfo client) {
        String guts = renderSelectBuilderPage(results,client);
        Server server = results.server;
        String title = "Data from server " + server.toString();
        String[] nav = new String[] {
            Replace.bracketQuote("Data from <a href=[/]>server</a> /" + server.linkTo()),
            search(results.search)
        };
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String renderColumnValueDistributionPage(SelectResults results, ClientInfo client) {
        return
            DistributionResultsTableRenderer.render(results,client) +
            PagingLinksRenderer.render(results) +
            AlternateDisplayLinksRenderer.render(results,serversStore,managedFunction)
        ;
    }

    String renderSelectBuilderPage(SelectResults results, ClientInfo client) {
        return
            ResultsTableRenderer.render(results,client) +
            PagingLinksRenderer.render(results) +
            ShowTableRenderer.render(results) +
            AlternateDisplayLinksRenderer.render(results,serversStore,managedFunction)
        ;
    }

}
