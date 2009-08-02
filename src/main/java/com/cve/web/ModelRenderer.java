package com.cve.web;

/**
 * To render models into a client-specific form.
 * Different renderers could be used to render a model into HTML,
 * or a JavaFX scene graph, for instance.
 * The type of thing the model is rendered to depends on the renderer.
 * <p>
 * This class looks like it would benefit from being generic, but toying
 * arounds shows that doing so doesn't go so well.  It makes life harder
 * for implementors.
 * @author curt
 */
public interface ModelRenderer {

    /**
     * Given a model, render it the way we do.
     */
    Object render(Model model, ClientInfo client);
}
