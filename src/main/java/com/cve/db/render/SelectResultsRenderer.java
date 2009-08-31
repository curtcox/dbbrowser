package com.cve.db.render;

import com.cve.db.SelectResults;
import com.cve.db.SelectResults.Type;
import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
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

    public SelectResultsRenderer() {}

    @Override
    public String render(Model model, ClientInfo client) {
        SelectResults results = (SelectResults) model;
        if (results.type==Type.COLUMN_VALUE_DISTRIBUTION) {
            return renderColumnValueDistributionPage(results,client);
        } else {
            return renderSelectBuilderPage(results,client);
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
