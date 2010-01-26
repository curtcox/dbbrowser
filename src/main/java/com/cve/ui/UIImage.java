package com.cve.ui;

import com.cve.html.HTMLTags;
import com.cve.log.Log;
import static com.cve.util.Check.notNull;
import java.net.URI;

/**
 * Like a HTML image.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UIImage implements UIElement {

    private final HTMLTags tags;
    private final String text;
    private final URI uri;

    private UIImage(String text, URI uri, Log log) {
        this.text = notNull(text);
        this.uri = notNull(uri);
        tags = HTMLTags.of(log);
    }
    
    public static UIImage textURI(String text, URI uri, Log log) {
        return new UIImage(text,uri,log);
    }

    @Override
    public String toString() {
        return tags.img(text, uri);
    }
}
