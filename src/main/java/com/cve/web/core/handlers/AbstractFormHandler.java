package com.cve.web.core.handlers;

import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;
import java.util.regex.Pattern;

/**
 * For handling pages with a form.
 * @author Curt
 */
public abstract class AbstractFormHandler
    implements RequestHandler
{
    /**
     * The stuff we handle.
     */
    private final Pattern pattern;

    public AbstractFormHandler(String regexp) {
        pattern = Pattern.compile(regexp);
    }

    public AbstractFormHandler() {
        pattern = Pattern.compile("");
    }

    /**
     * Return a response for this request, or null if this isn't the sort
     * of request we prodcuce responses for.
     */
    @Override
    public PageResponse produce(PageRequest request) {
        String uri = request.requestURI;
        if (!handles(uri)) {
            return null;
        }
        PageRequest.Method method = request.method;
        if (method==PageRequest.Method.GET) {
            return get(request);
        }
        if (method==PageRequest.Method.POST) {
            return post(request);
        }
        throw new IllegalArgumentException("" + method);
    }

    public boolean handles(String uri) {
        return pattern.matcher(uri).find();
    }

    public abstract PageResponse get(PageRequest request);
    public abstract PageResponse post(PageRequest request);

}
