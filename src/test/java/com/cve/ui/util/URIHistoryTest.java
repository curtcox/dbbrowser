package com.cve.ui.util;

import com.cve.util.URIs;
import java.net.URI;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class URIHistoryTest {

    final URI root = URIs.of("/");
    final URI a = URIs.of("/a");
    final URI b = URIs.of("/b");

    @Test public void follow() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        equals(a,history.current());
    }

    @Test public void followFollow() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        history.follow(b);
        equals(b,history.current());
    }

    @Test public void followBackGivesRoot() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        URI uri = history.back();
        equals(root,uri);
    }

    @Test public void followFollowBackGivesA() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        history.follow(b);
        URI uri = history.back();
        equals(a,uri);
    }

    @Test public void followBackForwardGivesA() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        history.back();
        URI uri = history.forward();
        equals(a,uri);
    }

    @Test public void followFollowBackForwardGivesB() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        history.follow(b);
        history.back();
        URI uri = history.forward();
        equals(b,uri);
    }

    @Test(expected=IllegalStateException.class)
    public void backAloneIsInvalid() {
        URIHistory.of().back();
    }

    @Test(expected=IllegalStateException.class)
    public void forwardAloneIsInvalid() {
        URIHistory.of().forward();
    }

    void equals(URI a, URI b) {
        assertEquals(a,b);
    }
}
