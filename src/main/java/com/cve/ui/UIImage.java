package com.cve.ui;

import com.cve.html.HTML;
import com.cve.util.Check;
import java.net.URI;

/**
 * Like a HTML image.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UIImage implements UIElement {

    private final String text;
    private final URI uri;

    private UIImage(String text, URI uri) {
        this.text = Check.notNull(text);
        this.uri = Check.notNull(uri);
    }
    
    public static UIImage textURI(String text, URI uri) {
        return new UIImage(text,uri);
    }

    @Override
    public String toString() {
        return HTML.img(text, uri);
    }
}
