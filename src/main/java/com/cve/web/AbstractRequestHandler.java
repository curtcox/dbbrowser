package com.cve.web;

import com.cve.log.Log;
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

    /**
     * Where we log to.
     */
    static final Log LOG = Log.of(AbstractRequestHandler.class);

    /**
     * Note in the log.
     */
    static void note(Object... o) {
        LOG.note(o);
    }

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
    @Override
    public PageResponse produce(PageRequest request) throws IOException, SQLException {
        note(request);
        String uri = request.requestURI;
        if (handles(uri)) {
            return get(request);
        }
        return null;
    }

    public boolean handles(String uri) {
        note(uri);
        return pattern.matcher(uri).find();
    }

    public abstract PageResponse get(PageRequest request) throws IOException, SQLException;
    
}
