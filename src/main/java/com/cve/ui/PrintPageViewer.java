package com.cve.ui;

import com.cve.lang.URIObject;
import com.cve.web.core.PageRequest;


/**
 * Not really a page viewer at all, but handy for testing.
 * @author curt
 */
public final class PrintPageViewer implements PageViewer {

    public static PageViewer of() {
        return new PrintPageViewer();
    }

    @Override
    public void browse(PageRequest request) {
        print("Requested " + request);
    }

    @Override
    public void browse(URIObject uri) {
        print("Requested " + uri);
    }

    static void print(String message) {
        System.out.println(message);
    }

}
