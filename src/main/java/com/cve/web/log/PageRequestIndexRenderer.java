package com.cve.web.log;

import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.log.LogEntry;
import com.cve.log.Logs;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageRequestProcessor;
import com.cve.web.db.NavigationButtons;
import java.net.URI;

/**
 * Renders a PageRequestIndex as a HTML page.
 * @author Curt
 */
final class PageRequestIndexRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private final HTMLTags tags = HTMLTags.of();

    private static URI HELP = URIs.of("/resource/help/request.html");

    private PageRequestIndexRenderer() {
        
    }

    public static PageRequestIndexRenderer of() {
        return new PageRequestIndexRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        PageRequestIndexModel page = (PageRequestIndexModel) model;
        String guts = tableOfEntries(page);
        String title = "Log Entries";
        NavigationButtons b = NavigationButtons.of();
        String[] nav = new String[] {};
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String tableOfEntries(PageRequestIndexModel index) {
        log.args(index);
        StringBuilder out = new StringBuilder();
        out.append(th("Request") + th("URI") + th("Entries") + th("Entries"));
        for (PageRequestProcessor id : index.requests) {
            PageRequestServiceModel page = index.pages.get(id);
            out.append(tr(
                td(id.linkTo().toString()   ,CSS.TABLE) +
                td(page.request.requestURI  ,CSS.TABLE) +
                td("" + page.entries.size() ,CSS.ROW_COUNT) +
                td(entriesFor(index,id)     ,CSS.COLUMN)
            ));
        }
        return table(out.toString());
    }

    String entriesFor(PageRequestIndexModel page, PageRequestProcessor id) {
        log.args(page,id);
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (LogEntry entry : page.pages.get(id).entries) {
            out.append(entry.linkTo() + " ");
            i++;
            if (i>20) {
                out.append("...");
                return out.toString();
            }
        }
        return out.toString();
    }

    String table(String s)       { return tags.table(s); }
    String tr(String s)          { return tags.tr(s); }
    String th(String s)          { return tags.th(s); }
    String th(String s, CSS css) { return tags.th(s, css); }
    String td(String s, CSS css) { return tags.td(s, css); }
}
