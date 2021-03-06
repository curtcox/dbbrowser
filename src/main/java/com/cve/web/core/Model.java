package com.cve.web.core;

/**
 * The logical version of something to be presented to the user.
 * This should arguable be PresentationModel instead.
 * A model more-or-less corresponds to a page.  As a matter of fact, model
 * classes are often indicated with a page name.
 * <p>
 * Models are turned into an actual page, or something else equivalent by
 * a renderer.
 * <p>
 * Generally, new pages require a RequestHandler, a new Model, and a Renderer.
 * @author curt
 */
public interface Model {

    /**
     * Use this in place of null
     */
    public static final Model NULL = new Model() {};
}
