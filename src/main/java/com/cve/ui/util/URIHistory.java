package com.cve.ui.util;

import com.cve.util.Check;
import com.cve.util.URIs;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;

/**
 * For managing a URI history with forward and back operations like in a web
 * browser.
 * @author curt
 */
public final class URIHistory {

    /**
     * Index of the current page.
     */
    int index = 1;

    /**
     * The list of pages in history.
     */
    final List<URI> pages = Lists.newArrayList();

    private URIHistory(URI root) {
        pages.add(Check.notNull(root));
    }

    public static URIHistory of() {
        return new URIHistory(URIs.of("/"));
    }

    /**
     * Follow the given link.
     */
    public void follow(URI uri) {
        if (index>=pages.size()) {
            pages.add(uri);
        } else {
            pages.set(index, uri);
        }
        index++;
    }

    /**
     * Move forward a page in history.
     */
    public URI forward() {
        if (index>=pages.size()) {
            throw new IllegalStateException();
        }
        index++;
        return current();
    }

    /**
     * Move backward a page in history.
     */
    public URI back() {
        if (index<2) {
            throw new IllegalStateException();
        }
        index--;
        return current();
    }

    /**
     * Return the current page.
     */
    public URI current() {
        return pages.get(index-1);
    }

}
