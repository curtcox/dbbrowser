package com.cve.web.core;

/**
 * A unified interface for handling page requests.
 * Implementers need to be models that handle their own requests and rendering.
 * Note that this is a stronger requirement than merely implementing all three interfaces.
 * For instance, it is not appropriate to implement this interface for when the RequestHandler
 * produces multiple different model types (such as index and detail models)
 * depending on the request.
 * <p>
 * Use of the combined interface tends to prodcue one larger implementation class
 * and less wiring code.
 * @see AbstractPage
 * @author curt
 */
public interface Page extends RequestHandler, Model, ModelHtmlRenderer {

}
