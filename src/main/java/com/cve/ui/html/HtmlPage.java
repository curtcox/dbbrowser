package com.cve.ui.html;

import com.cve.web.core.*;
import static com.cve.util.Check.notNull;
import javax.annotation.concurrent.Immutable;
import com.cve.ui.HTMLTags;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.URIs;
import java.net.URI;

/**
 * An immutable structured representation of an HtmlPage.
 * The kind of pages we represent have several standard parts.
 * <ol>
 * <li> base, head, title, and body correspond to specific HTML entities
 * <li> help, content, and navigation don't
 * </ol>
 * For a nice reference, see
 * http://en.wikipedia.org/wiki/HTML_element
 */
@Immutable
final class HtmlPage {

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

    /**
     * We used this for unspecified URIs.
     */
    private static final URI EMPTY = URIs.of("");

    public final Log log = Logs.of();

    final HTMLTags tags;

    /**
     * Link to the we server root directory.
     */
    private static final String HOME = home();

    static String home() {
        String tip = "Home";
        Label text = Label.of("Home");
        URI target = URIs.of("/");
        URI  image = Icons.HOME;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    static String help(URI uri) {
        String tip = "Help";
        Label text = Label.of("Help");
        URI target = uri;
        URI  image = Icons.HELP;
        return Link.textTargetImageAlt(text,target,image,tip).toString();
    }

    private HtmlPage(String title, String guts, String[] navigation, URI base, URI help, String head, String body) {
        this.title = notNull(title);
        this.guts = notNull(guts);
        this.navigation = notNull(navigation);
        this.base = notNull(base);
        this.help = notNull(help);
        this.head = notNull(head);
        this.body = notNull(body);
        
        tags = HTMLTags.of();
    }

    public static HtmlPage guts(String guts) {
        String title = "";
        String[] navigation = new String[] {};
        URI base = EMPTY;
        URI help = EMPTY;
        String head = "";
        String body = body(title,guts,navigation,help);
        return new HtmlPage(title, guts, navigation, base, help, head, body);
    }

    public static HtmlPage gutsHelp(String guts, URI help) {
        String title = "";
        String[] navigation = new String[] {};
        URI base = EMPTY;
        String head = "";
        String body = body(title,guts,navigation,help);
        return new HtmlPage(title, guts, navigation, base, help, head, body);
    }

    public static HtmlPage gutsTitleNavHelp(String guts, String title, String[] nav, URI help) {
        URI base = EMPTY;
        String head = "";
        String body = body(title,guts,nav,help);
        return new HtmlPage(title, guts, nav, base, help, head, body);
    }

    public static HtmlPage gutsTitleNavHelpBase(String guts, String title, String[] nav, URI help, URI base) {
        HTMLTags tags = HTMLTags.of();
        String head = tags.base(base);
        String body = body(title,guts,nav,help);
        return new HtmlPage(title, guts, nav, base, help, head, body);
    }

    static String body(String title, String guts, String nav[], URI help) {
        StringBuilder out = new StringBuilder();
        HTMLTags tags = HTMLTags.of();
        out.append(tags.td(HOME));
        out.append(tags.td(help(help)));
        for (String n : nav) {
            out.append(tags.td(n));
        }
        return tags.title(title) + tags.table(tags.tr(out.toString())) + guts;
    }

    public HtmlPage withGuts(String guts) {
        String newBody = body(title,guts,navigation,help);
        return new HtmlPage(title, guts, navigation, base, help, head, newBody);
    }

    @Override
    public String toString() {

        if (head.isEmpty()) {
            return tags.html(tags.body(body));
        }
        return tags.html(tags.head(head) + tags.body(body));
    }
}
