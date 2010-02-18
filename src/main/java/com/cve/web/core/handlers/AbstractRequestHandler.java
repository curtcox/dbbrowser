package com.cve.web.core.handlers;

import java.util.regex.Pattern;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;

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
    private final Log log = Logs.of();

    /**
     * Create a new handler that services the request types specified by
     * the supplied regular expression string.
     * @param regexp
     */
    public AbstractRequestHandler(String regexp) {
        
        pattern = Pattern.compile(regexp);
    }

    /**
     * Construct a new handler with no associated regex.
     * If you use this, you should override handles, too.
     */
    public AbstractRequestHandler() {
        
        pattern = Pattern.compile("");
    }

    /**
     * Return a response for this request, or null if this isn't the sort
     * of request we prodcuce responses for.
     * <p>
     * While it is sometimes necessary to override this method, generally
     * overriding get is the right thing to do instead.
     */
    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);
        String uri = request.requestURI;
        if (handles(uri)) {
            return PageResponse.of(request,get(request));
        }
        return null;
    }

    /**
     * Return true if this is the sort of request we handle, or false
     * otherwise.  This method checks the regex supplied in the constructor
     * and is used by the supplied implementation of produce.
     */
    public boolean handles(String uri) {
        log.args(uri);
        return pattern.matcher(uri).find();
    }

    /**
     * Usually, implementors will just provide this method, plus constructor args.
     */
    public abstract Model get(PageRequest request);
    
}
