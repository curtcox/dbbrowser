package com.cve.web.core;

import com.cve.ui.UIElement;


/**
 * To render models to a UIElement.  This interface is designed to allow
 * chains of renderers.  Consider PageDecorator.
 * The UIElement that is produced could be used to produce HTML, a Swing UI,
 * or a JavaFX scene graph, for instance.
 * <p>
 * ModelRenderers are usually used by more generic ModelRenderers for delegation.
 * The exception is SelfRenderingModelS.
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
     * However, it is also common and reasonable for Renderers to immediately
     * cast from model to whatever they handle.  These Renderers will need to
     * be guarded (with a ClassMap, for instance), to ensure they aren't given
     * the wrong kind of model.
     * <p>
     * See PageDecorator.
     */
    UIElement render(Model model, ClientInfo client);

}
