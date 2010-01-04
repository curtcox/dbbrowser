package com.cve.web;

import com.cve.stores.ManagedFunction;
import com.cve.stores.Stores;

/**
 *
 * @author Curt
 */
public class WebApp {

    final RequestHandler handler;
    final ModelHtmlRenderer renderer;

    interface Context {
        ManagedFunction.Factory getManagedFunctionFactory();
        Stores getStores();
    }

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
