package com.cve.web;

/**
 * To render models to a HTML string.
 * A parallel interface could be used to render a model into a JavaFX scene
 * graph, for instance.
 */
public interface ModelHtmlRenderer {

    /**
     * Given a model, render it the way we do.
     */
    String render(Model model, ClientInfo client);
}
