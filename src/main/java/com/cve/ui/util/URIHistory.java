package com.cve.ui.util;

import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;

/**
 *
 * @author curt
 */
public final class URIHistory {

    int index;
    final List<URI> pages = Lists.newArrayList();

    public static URIHistory of() {
        return new URIHistory();
    }

    public void follow(URI uri) {
    }

    public URI forward() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public URI back() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
