package com.cve.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Something that handles {@link PageRequest}S and produces
 * {@link URIResponse}S.
 * @author Curt
 */
public abstract class AbstractRequestHandler
    implements RequestHandler
{

    /**
     * The stuff we handle.
     */
    private final Pattern pattern;

    public AbstractRequestHandler(String regexp) {
        pattern = Pattern.compile(regexp);
    }

    public AbstractRequestHandler() {
        pattern = Pattern.compile("");
    }

    /**
     * Return a response for this request, or null if this isn't the sort
     * of request we prodcuce responses for.
     */
    public PageResponse produce(PageRequest request) throws IOException, SQLException {
        String uri = request.getRequestURI();
        if (handles(uri)) {
            return doProduce(request);
        }
        return null;
    }

    public boolean handles(String uri) {
        return pattern.matcher(uri).find();
    }

    public abstract PageResponse doProduce(PageRequest request) throws IOException, SQLException;
    
}
