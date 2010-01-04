package com.cve.web;

/**
 *
 * @author Curt
 */
final class SimpleWebApp implements WebApp {

    private final RequestHandler handler;
    private final ModelHtmlRenderer renderer;

    private SimpleWebApp(RequestHandler handler, ModelHtmlRenderer renderer) {
        this.handler = handler;
        this.renderer = renderer;
    }

    public static WebApp of(RequestHandler handler, ModelHtmlRenderer renderer) {
        return new SimpleWebApp(handler,renderer);
    }

    @Override
    public RequestHandler getRequestHandler() {
        return handler;
    }

    @Override
    public ModelHtmlRenderer getModelHtmlRenderer() {
        return renderer;
    }

}
