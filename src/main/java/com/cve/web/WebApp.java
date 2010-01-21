package com.cve.web;

import com.cve.log.Log;
import static com.cve.util.Check.notNull;

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
     * Where we log to.
     */
    final Log log;

    /**
     * Use the factory.
     */
    private WebApp(RequestHandler handler, ModelHtmlRenderer renderer, Log log) {
        this.handler = notNull(handler);
        this.renderer = notNull(renderer);
        this.log = notNull(log);
    }

    public static WebApp of(RequestHandler handler, ModelHtmlRenderer renderer, Log log) {
        return new WebApp(handler,renderer,log);
    }

}
