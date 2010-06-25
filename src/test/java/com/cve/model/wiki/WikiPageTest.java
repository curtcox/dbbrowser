package com.cve.model.wiki;

import com.cve.lang.URIObject;
import com.cve.model.test.Assert;

/**
 *
 * @author curt
 */
public class WikiPageTest {

    WikiPage page() {
        return WikiPage.of();
    }

    public void translatesPathsToResources() {
        WikiPage page = page();
        URIObject.Path path = URIObject.Path.of("/foo");
        WikiResource resource = page.translate(path);
        Assert.notNull(resource);
    }

    public void displaysResources() {
        Assert.failure();
    }

    public void editsResources() {
        Assert.failure();
    }

    public void savesResources() {
        Assert.failure();
    }

    public void transformsResourcesOnDisplay() {
        Assert.failure();
    }

    public void transformsResourcesOnSave() {
        Assert.failure();
    }

    public void viewTheTests() {
        Assert.failure();
    }

    public void viewTheTestResults() {
        Assert.failure();
    }

    public void runTheTests() {
        Assert.failure();
    }

    public void editTheTests() {
        Assert.failure();
    }
}
