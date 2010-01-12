package com.cve.web;

import com.cve.log.Log;
import java.util.regex.Pattern;
import static com.cve.log.Log.args;

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
        args(request);
        String uri = request.requestURI;
        if (handles(uri)) {
            return PageResponse.of(get(request));
        }
        return null;
    }

    /**
     * Return true if this is the sort of request we handle, or false
     * otherwise.  This method checks the regex supplied in the constructor
     * and is used by the supplied implementation of produce.
     */
    public boolean handles(String uri) {
        args(uri);
        return pattern.matcher(uri).find();
    }

    /**
     * Usually, implementors will just provide this method, plus constructor args.
     */
    public abstract Model get(PageRequest request);
    
}
