package com.cve.web;

/**
 * Something that handles {@link PageRequest}S and produces
 * {@link URIResponse}S.  Generally, new pages will require a
 * RequestHandler, a new Model, and a Renderer, so see those too.
 * @author Curt
 */
public interface RequestHandler {

    public interface Factory {
        RequestHandler of();
    }

    /**
     * Return a response for this request, or null if this isn't the sort
     * of request we prodcuce responses for.
     * <p>
     * Note that this method doesn't declare any exceptions being thrown.
     * That's not because we expect everything to always be fine.
     * It is quite the opposite in fact.  Handlers should use CurrentValueS
     * to handle things like timeouts and connection failures gracefully.
     * Generally, that means using the most recent available data and noting
     * any error conditions known to exist.
     */
    PageResponse produce(PageRequest request);

}
