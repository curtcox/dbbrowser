package com.cve.web.db.render;

import com.cve.lang.URIObject;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.SelectResults;
import com.cve.model.db.SelectResults.Type;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.ui.UIElement;
import com.cve.util.Replace;
import com.cve.util.URIs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.db.NavigationButtons;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

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

    final DBServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    final Log log = Logs.of();

    private static URIObject HELP = URIs.of("/resource/help/SelectResults.html");

    private SelectResultsRenderer(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
           this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
        
    }

    public static SelectResultsRenderer of(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new SelectResultsRenderer(serversStore,managedFunction);
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        log.args(model,client);
        SelectResults results = (SelectResults) model;
        if (results.type==Type.COLUMN_VALUE_DISTRIBUTION) {
            return renderColumnValueDistribution(results,client);
        } else {
            return renderNormalResults(results,client);
        }
    }

    public UIElement renderColumnValueDistribution(SelectResults results, ClientInfo client) {
        String guts = renderColumnValueDistributionPage(results,client);
        String title = "Values in " + results.select.columns.get(0);
        NavigationButtons b = NavigationButtons.of();
        String[] nav = new String[] { b.search(results.search) };
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    public UIElement renderNormalResults(SelectResults results, ClientInfo client) {
        String guts = renderSelectBuilderPage(results,client);
        DBServer server = results.server;
        String title = "Data from server " + server.toString();
        NavigationButtons b = NavigationButtons.of();
        String[] nav = new String[] {
            Replace.bracketQuote("Data from <a href=[/]>server</a> /" + server.linkTo()),
            b.search(results.search)
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
