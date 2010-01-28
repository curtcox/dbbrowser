package com.cve.web;

import com.cve.log.Log;
import com.cve.web.log.AnnotatedStackTraceModel;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * The response to a page request.
 * Note that a response is not tied to a particular type of client.
 * Different renderers will transform the response into something client
 * specific like HTML or a scene graph.
 * The payload of a response is either a model or a redirect.
 * @author curt
 */
@Immutable
public final class PageResponse {

    /**
     * The request that this is a response for.
     */
    public final PageRequest request;

    /**
     * Possibly null URI to redirect to.
     */
    public final URI redirect;

    /**
     * Possibly null Model to render.
     */
    public final Model model;

    /**
     * Where we log to.
     */
    public final Log log;

    /**
     * Normal constructor -- use a factory.
     */
    private PageResponse(PageRequest request, Model model, Log log) {
        this.request = notNull(request);
        this.model = notNull(model);
        redirect   = null;
        this.log = notNull(log);
    }

    /**
     * Redirect constructor -- use a factory.
     */
    private PageResponse(PageRequest request,URI redirect, Log log) {
        this.request = notNull(request);
        this.model    = null;
        this.redirect = notNull(redirect);
        this.log = notNull(log);
    }

    public static PageResponse of(PageRequest request,Throwable throwable, Log log) {
        return of(request,AnnotatedStackTraceModel.throwable(throwable,log),log);
    }

    public static PageResponse of(PageRequest request,byte[] bytes,ContentType type, Log log) {
        return of(request,ByteArrayModel.bytesType(bytes,type),log);
    }

    public static PageResponse of(PageRequest request,Model model, Log log) {
        return new PageResponse(request,model,log);
    }

    public static PageResponse newRedirect(PageRequest request,URI dest, Log log) {
        return new PageResponse(request,dest,log);
    }

    @Override
    public String toString() {
        return "<PageResponse>" +
                    " request=" + request +
                    " redirect=" + redirect +
                    " model=" + model +
               "<PageResponse>";
    }
}
