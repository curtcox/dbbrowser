package com.cve.web;

import com.cve.util.Check;
import javax.annotation.concurrent.Immutable;
import com.cve.html.HTML;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.html.SimpleTooltip;
import com.cve.html.Tooltip;
import com.cve.util.URIs;
import java.net.URI;
import static com.cve.html.HTML.*;

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

    /**
     * Link to the we server root directory.
     */
    private static final String HOME = home();

    static String home() {
        Tooltip tip = SimpleTooltip.of("Home");
        Label text = Label.of("Home");
        URI target = URIs.of("/");
        URI  image = URIs.of("/resource/icons/actions/go-home.png");
        return Link.textTargetTipImage(text,target,tip,image).toString();
    }

    static String help(URI uri) {
        Tooltip tip = SimpleTooltip.of("Help");
        Label text = Label.of("Help");
        URI target = uri;
        URI  image = URIs.of("/resource/icons/apps/help-browser.png");
        return Link.textTargetTipImage(text,target,tip,image).toString();
    }

    private HtmlPage(String title, String guts, String[] navigation, URI base, URI help, String head, String body) {
        this.title = Check.notNull(title);
        this.guts = Check.notNull(guts);
        this.navigation = Check.notNull(navigation);
        this.base = Check.notNull(base);
        this.help = Check.notNull(help);
        this.head = Check.notNull(head);
        this.body = Check.notNull(body);
    }

    public static HtmlPage guts(String guts) {
        String title = "";
        String[] navigation = new String[] {};
        URI base = EMPTY;
        URI help = EMPTY;
        String head = "";
        String body = body(guts,navigation,help);
        return new HtmlPage(title, guts, navigation, base, help, head, body);
    }

    public static HtmlPage gutsHelp(String guts, URI help) {
        String title = "";
        String[] navigation = new String[] {};
        URI base = EMPTY;
        String head = "";
        String body = body(guts,navigation,help);
        return new HtmlPage(title, guts, navigation, base, help, head, body);
    }

    public static HtmlPage gutsTitleNavHelp(String guts, String title, String[] nav, URI help) {
        URI base = EMPTY;
        String head = "";
        String body = body(guts,nav,help);
        return new HtmlPage(title, guts, nav, base, help, head, body);
    }

    public static HtmlPage gutsTitleNavHelpBase(String guts, String title, String[] nav, URI help, URI base) {
        String head = base(base);
        String body = nav + guts;
        return new HtmlPage(title, guts, nav, base, help, head, body);
    }

    static String body(String guts, String nav[], URI help) {
        StringBuilder out = new StringBuilder();
        out.append(td(HOME));
        out.append(td(help(help)));
        for (String n : nav) {
            out.append(td(n));
        }
        return table(tr(out.toString())) + guts;
    }

    public HtmlPage withGuts(String guts) {
        String body = body(guts,navigation,help);
        return new HtmlPage(title, guts, navigation, base, help, head, body);
    }

    @Override
    public String toString() {
        if (head.isEmpty()) {
            return HTML.html(HTML.body(body));
        }
        return HTML.html(HTML.head(head) + HTML.body(body));
    }
}
