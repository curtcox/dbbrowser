package com.cve.web;

import com.cve.log.Log;
import java.util.regex.Pattern;
import static com.cve.util.Check.notNull;

/**
 * Something that handles {@link PageRequest}S and produces
 * {@link URIResponse}S.
 * @author Curt
 */
public abstract class AbstractBinaryRequestHandler
    implements RequestHandler
{

    /**
     * The stuff we handle.
     */
    private final Pattern pattern;

    /**
     * The type of thing we produce.
     */
    private final ContentType type;

    /**
     * Where we log to.
     */
    private final Log log;

    public AbstractBinaryRequestHandler(String regexp, ContentType type, Log log) {
        notNull(regexp);
        notNull(type);
        this.log = notNull(log);
        pattern = Pattern.compile(regexp);
        this.type = type;
    }

    public AbstractBinaryRequestHandler(ContentType type, Log log) {
        this.log = notNull(log);
        this.type = notNull(type);
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
            return PageResponse.of(get(request),type);
        }
        return null;
    }

    public boolean handles(String uri) {
        log.args(uri);
        return pattern.matcher(uri).find();
    }

    /**
     * Usually, implementors will just provide this method, plus constructor args.
     */
    public abstract byte[] get(PageRequest request);
    
}
