package com.cve.web.core;

import com.cve.lang.URIObject;
import com.cve.ui.UIElement;

import javax.annotation.concurrent.Immutable;


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
public final class HtmlPage
    implements UIElement
{
    final String guts;
    final String javascript;
    final String css;
    final String title;
    final String[] nav;
    final URIObject help;

    public static UIElement guts(UIElement... elements) {
        throw new UnsupportedOperationException();
    }

    private static UIElement guts(String guts) {
        throw new UnsupportedOperationException();
    }

    public static UIElement gutsHelp(String guts, URIObject help) {
        throw new UnsupportedOperationException();
    }

    public static UIElement gutsTitleNavHelp(String guts, String title, String[] nav, URIObject help) {
        return new HtmlPage(guts,null,null,title,nav,help);
    }

    public static UIElement javascriptCssGuts(String javascript, String css, UIElement page) {
        return new HtmlPage(page.toString(),javascript,css,"title",new String[0],null);
    }

    public static UIElement gutsTitleNavHelpBase(String guts, String title, String[] nav, URIObject help, URIObject base) {
        throw new UnsupportedOperationException();
    }

    HtmlPage(String guts, String javascript, String css, String title, String[] nav, URIObject help) {
        this.guts = guts;
        this.javascript = javascript;
        this.css = css;
        this.title = title;
        this.nav = nav;
        this.help = help;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("<html>");
        out.append("<body>");
        if (javascript!=null) { out.append(javascript); }
        if (css!=null)        { out.append(css); }
        if (guts!=null)       { out.append(guts); }
        out.append("</body>");
        out.append("</html>");
        System.out.println(out.toString());
        return out.toString();
    }
}
