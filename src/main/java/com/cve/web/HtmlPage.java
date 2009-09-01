package com.cve.web;

import com.cve.util.Check;
import javax.annotation.concurrent.Immutable;
import com.cve.html.HTML;

/**
 * An immutable structured representation of a HtmlPage.
 * @author curt
 */
@Immutable
public final class HtmlPage {

    public final String head;
    public final String body;

    private HtmlPage(String head, String body) {
        this.head = Check.notNull(head);
        this.body = Check.notNull(body);
    }

    public static HtmlPage of(String head, String body) {
        return new HtmlPage(head,body);
    }

    public static HtmlPage headBody(String head, String body) {
        return new HtmlPage(head,body);
    }

    public static HtmlPage body(String body) {
        return new HtmlPage("",body);
    }

    @Override
    public String toString() {
        if (head.isEmpty()) {
            return HTML.html(HTML.body(body));
        }
        return HTML.html(HTML.head(head) + HTML.body(head));
    }
}
