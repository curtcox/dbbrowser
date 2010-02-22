package com.cve.web.core;

import static com.cve.util.Check.notNull;

/**
 * A web application
 * @author Curt
 */
public final class WebApp {

    /**
     * Turns requests into models.
     */
    public final RequestHandler handler;

    /**
     * Turns models into pages.
     */
    public final ModelHtmlRenderer renderer;

    /**
     * Use the factory.
     */
    private WebApp(RequestHandler handler, ModelHtmlRenderer renderer) {
        this.handler = notNull(handler);
        this.renderer = notNull(renderer);
        
    }

    public static WebApp of(RequestHandler handler, ModelHtmlRenderer renderer) {
        return new WebApp(handler,renderer);
    }

}
