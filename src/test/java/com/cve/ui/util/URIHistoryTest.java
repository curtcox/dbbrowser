package com.cve.ui.util;

import com.cve.lang.URIObject;
import com.cve.util.URIs;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class URIHistoryTest {

    final URIObject root = URIs.of("/");
    final URIObject a = URIs.of("/a");
    final URIObject b = URIs.of("/b");

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
        URIObject uri = history.back();
        equals(root,uri);
    }

    @Test public void followFollowBackGivesA() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        history.follow(b);
        URIObject uri = history.back();
        equals(a,uri);
    }

    @Test public void followBackForwardGivesA() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        history.back();
        URIObject uri = history.forward();
        equals(a,uri);
    }

    @Test public void followFollowBackForwardGivesB() {
        URIHistory history = URIHistory.of();
        history.follow(a);
        history.follow(b);
        history.back();
        URIObject uri = history.forward();
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

    void equals(URIObject a, URIObject b) {
        assertEquals(a,b);
    }
}
