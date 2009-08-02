package com.cve.web;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Something that handles {@link PageRequest}S and produces
 * {@link URIResponse}S.
 * @author Curt
 */
public interface RequestHandler {

    /**
     * Return a response for this request, or null if this isn't the sort
     * of request we prodcuce responses for.
     */
    PageResponse produce(PageRequest request) throws IOException, SQLException;

}
