package com.cve.web.core;

import javax.annotation.concurrent.Immutable;
import com.cve.ui.UIElement;
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
public final class HtmlPage {


    public static UIElement guts(String guts) {
        throw new UnsupportedOperationException();
    }

    public static UIElement gutsHelp(String guts, URI help) {
        throw new UnsupportedOperationException();
    }

    public static UIElement gutsTitleNavHelp(String guts, String title, String[] nav, URI help) {
        throw new UnsupportedOperationException();
    }

    public static UIElement gutsTitleNavHelpBase(String guts, String title, String[] nav, URI help, URI base) {
        throw new UnsupportedOperationException();
    }

}
