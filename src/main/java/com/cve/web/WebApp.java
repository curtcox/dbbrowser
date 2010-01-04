package com.cve.web;

/**
 *
 * @author Curt
 */
public interface WebApp {

    RequestHandler getRequestHandler();

    ModelHtmlRenderer getModelHtmlRenderer();

}
