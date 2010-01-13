package com.cve.web;

/**
 * A web application
 * @author Curt
 */
public final class WebApp {

    /**
     * Turns requests into models.
     */
    final RequestHandler handler;

    /**
     * Turns models into pages.
     */
    final ModelHtmlRenderer renderer;

    /**
     * Use the factory.
     */
    private WebApp(RequestHandler handler, ModelHtmlRenderer renderer) {
        this.handler = handler;
        this.renderer = renderer;
    }

    public static WebApp of(RequestHandler handler, ModelHtmlRenderer renderer) {
        return new WebApp(handler,renderer);
    }

}
