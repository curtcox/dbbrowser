package com.cve.web.core;

import com.cve.log.Log;
import com.cve.log.Logs;
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
    final Log log = Logs.of();

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
