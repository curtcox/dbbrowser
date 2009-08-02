package com.cve.web;

/**
 * Determines the proper ContentType (MIME type) for a rendered model.
 * @author curt
 */
interface ContentTyper {

    /**
     * Given that the model was rendered to the given object, what kind
     * of MIME type should be sent to the client?
     */
    ContentType type(Model model, Object rendered);
}
