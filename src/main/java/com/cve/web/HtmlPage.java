package com.cve.web;

import static com.cve.util.Check.notNull;
import javax.annotation.concurrent.Immutable;
import com.cve.html.HTMLTags;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.SimpleLog;
import com.cve.util.URIs;
import java.net.URI;

/**
 * An immutable structured representation of a HtmlPage.
 * The kind of pages we represent have several standard parts.
 * <ol>
 * <li> base, head, title, and body correspond to specific HTML entities
 * <li> help, content, and navigation don't
 * </ol>
 * For a nice reference, see
 * http://en.wikipedia.org/wiki/HTML_element
 */
@Immutable
public final class HtmlPage {

    /**
     * Base URI for this page.
     */
    public final URI base;
    
    /**
     * Help page for this page
     */
    public final URI help;

    /**
     * What this page is all about.
     */
    public final String guts;

    /**
     * Contents of title tag for this page.
     */
    public final String title;

    /**
     * HTML fragment linking to other pages
     */
    public final String[] navigation;

    /**
     * Contents of the head tag of this page
     */
    public final String head;

    /**
     * Contents of the body tag of this page.
     */
    public final String body;

    private static final URI EMPTY = URIs.of("");

    public final Log log;

    private static final Log LOG = SimpleLog.of(HtmlPage.class);

    final HTMLTags tags;

    /**
     * Link to the we server root directory.
     */
    private static final String HOME = home(LOG);

    static String home(Log log) {
        String tip = "Home";
        Label text = Label.of("Home",log);
        URI target = URIs.of("/");
        URI  image = Icons.HOME;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    static String help(URI uri, Log log) {
        String tip = "Help";
        Label text = Label.of("Help",log);
        URI target = uri;
        URI  image = Icons.HELP;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    private HtmlPage(String title, String guts, String[] navigation, URI base, URI help, String head, String body, Log log) {
        this.title = notNull(title);
        this.guts = notNull(guts);
        this.navigation = notNull(navigation);
        this.base = notNull(base);
        this.help = notNull(help);
        this.head = notNull(head);
        this.body = notNull(body);
        this.log = notNull(log);
        tags = HTMLTags.of(log);
    }

    public static HtmlPage guts(String guts, Log log) {
        String title = "";
        String[] navigation = new String[] {};
        URI base = EMPTY;
        URI help = EMPTY;
        String head = "";
        String body = body(title,guts,navigation,help,log);
        return new HtmlPage(title, guts, navigation, base, help, head, body,log);
    }

    public static HtmlPage gutsHelp(String guts, URI help, Log log) {
        String title = "";
        String[] navigation = new String[] {};
        URI base = EMPTY;
        String head = "";
        String body = body(title,guts,navigation,help,log);
        return new HtmlPage(title, guts, navigation, base, help, head, body,log);
    }

    public static HtmlPage gutsTitleNavHelp(String guts, String title, String[] nav, URI help, Log log) {
        URI base = EMPTY;
        String head = "";
        String body = body(title,guts,nav,help,log);
        return new HtmlPage(title, guts, nav, base, help, head, body,log);
    }

    public static HtmlPage gutsTitleNavHelpBase(String guts, String title, String[] nav, URI help, URI base, Log log) {
        HTMLTags tags = HTMLTags.of(log);
        String head = tags.base(base);
        String body = body(title,guts,nav,help,log);
        return new HtmlPage(title, guts, nav, base, help, head, body,log);
    }

    static String body(String title, String guts, String nav[], URI help, Log log) {
        StringBuilder out = new StringBuilder();
        HTMLTags tags = HTMLTags.of(log);
        out.append(tags.td(HOME));
        out.append(tags.td(help(help,log)));
        for (String n : nav) {
            out.append(tags.td(n));
        }
        return tags.title(title) + tags.table(tags.tr(out.toString())) + guts;
    }

    public HtmlPage withGuts(String guts) {
        String newBody = body(title,guts,navigation,help,log);
        return new HtmlPage(title, guts, navigation, base, help, head, newBody,log);
    }

    @Override
    public String toString() {

        if (head.isEmpty()) {
            return tags.html(tags.body(body));
        }
        return tags.html(tags.head(head) + tags.body(body));
    }
}
