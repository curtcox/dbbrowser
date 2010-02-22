package com.cve.web.management;

import com.cve.html.CSS;
import com.cve.ui.HTMLTags;
import com.cve.log.Log;
import com.cve.log.LogEntry;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.util.URIs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.PageRequestProcessor;
import com.cve.web.db.NavigationButtons;
import java.net.URI;

/**
 *
 * @author Curt
 */
final class PageRequestModelRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final ObjectLink link = ObjectLink.of();

    private final HTMLTags tags = HTMLTags.of();

    private static URI HELP = URIs.of("/resource/help/request.html");

    private PageRequestModelRenderer() {}

    public static PageRequestModelRenderer of() {
        return new PageRequestModelRenderer();
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        log.args(model,client);
        PageRequestServiceModel page = (PageRequestServiceModel) model;
        String guts = tableOfEntries(page);
        String title = "Log Entries";
        NavigationButtons b = NavigationButtons.of();
        String[] nav = new String[] {};
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String tableOfEntries(PageRequestServiceModel page) {
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
