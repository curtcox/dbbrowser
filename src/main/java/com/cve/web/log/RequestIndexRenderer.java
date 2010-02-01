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
import com.cve.web.PageRequest;
import com.cve.web.db.NavigationButtons;
import java.net.URI;

/**
 *
 * @author Curt
 */
final class RequestIndexRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private final HTMLTags tags = HTMLTags.of();

    private static URI HELP = URIs.of("/resource/help/request.html");

    private RequestIndexRenderer() {
        
    }

    public static RequestIndexRenderer of() {
        return new RequestIndexRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        RequestIndexModel page = (RequestIndexModel) model;
        String guts = tableOfEntries(page);
        String title = "Log Entries";
        NavigationButtons b = NavigationButtons.of();
        String[] nav = new String[] {};
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String tableOfEntries(RequestIndexModel page) {
        log.args(page);
        StringBuilder out = new StringBuilder();
        out.append(th("Request ID") + th("Entries") + th("Entries"));
        for (PageRequest.ID id : page.requests) {
            out.append(tr(
                td(id.linkTo().toString()           ,CSS.TABLE) +
                td("" + page.entries.get(id).size() ,CSS.ROW_COUNT) +
                td(entriesFor(page,id)              ,CSS.COLUMN)
            ));
        }
        return table(out.toString());
    }

    String entriesFor(RequestIndexModel page, PageRequest.ID id) {
        log.args(page,id);
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (LogEntry entry : page.entries.get(id)) {
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
