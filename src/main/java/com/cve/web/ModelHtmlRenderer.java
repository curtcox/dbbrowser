package com.cve.web;

/**
 * To render models to a HTML string.  This interface is designed to allow
 * chains of renderers.  Consider PageDecorator.
 * A parallel interface could be used to render a model into a JavaFX scene
 * graph, for instance.
 * <p>
 * Should this interface have a type parameter?
 * <p>
 * Generally, new pages require a RequestHandler, a new Model, and a Renderer.
 */
public interface ModelHtmlRenderer {

    /**
     * Given a model, render it the way we do.
     * The string returned may be the entire page, just the body, or the body
     * contents with a head prepended.
     * <p>
     * Generally, renderers should return null, if they don't render the model
     * they are given, rather than throw an exception.  This allows the 
     * chaining and composition of renderers.
     * <p>
     * See PageDecorator.
     */
    HtmlPage render(Model model, ClientInfo client);
}
