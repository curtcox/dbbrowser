package com.cve.model.wiki;

import com.cve.lang.URIObject.Path;
import com.cve.web.core.pages.AbstractPage;

/**
 * A wiki:
 * <ul>
    <li> translates paths to resources
    <li> displays resources
    <li> edits resources
    <li> saves resources
    <li> transforms resources on display
    <li> transforms resources on save
 * </ul>
 * <p>
 * If the page is also a code page, that also means:
 * <ul>
 *   <li> view the tests
 *   <li> view the test results
 *   <li> run the tests
 *   <li> edit the tests
 * </ul>
 * @author curt
 */
public final class WikiPage extends AbstractPage {

    static WikiPage of() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    WikiResource translate(Path path) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
