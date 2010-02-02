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
final class RequestModelRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final ObjectLink link = ObjectLink.of();

    private final HTMLTags tags = HTMLTags.of();

    private static URI HELP = URIs.of("/resource/help/request.html");

    private RequestModelRenderer() {}

    public static RequestModelRenderer of() {
        return new RequestModelRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        RequestModel page = (RequestModel) model;
        String guts = tableOfEntries(page);
        String title = "Log Entries";
        NavigationButtons b = NavigationButtons.of();
        String[] nav = new String[] {};
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String tableOfEntries(RequestModel page) {
        log.args(page);
        StringBuilder out = new StringBuilder();
        out.append(th("Timestamp") + th("Level") + th("Logger") + th("Message") + th("Trace") + th("Args"));
        for (LogEntry entry : page.entries) {
            out.append(tr(
                td(entry.timeStamp.toString()       ,CSS.TABLE) +
                td(entry.level.toString()           ,CSS.ROW_COUNT) +
                td(entry.logger.toString()          ,CSS.ROW_COUNT) +
                td(entry.message.toString()         ,CSS.ROW_COUNT) +
                td(entry.trace.linkTo().toString()  ,CSS.ROW_COUNT) +
                td(argLinks(entry.args)             ,CSS.ROW_COUNT)
            ));
        }
        return table(out.toString());
    }

    String argLinks(Object[] args) {
        StringBuilder out = new StringBuilder();
        for (Object arg : args) {
            out.append(link.to(arg));
        }
        return out.toString();
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
