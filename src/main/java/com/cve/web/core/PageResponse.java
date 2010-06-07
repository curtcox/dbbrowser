package com.cve.web.core;

import com.cve.lang.URIObject;
import com.cve.lang.AnnotatedCallTree;
import com.cve.web.core.models.ByteArrayModel;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.management.AnnotatedStackTraceModel;

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
     * Possibly null URIObject to redirect to.
     */
    public final URIObject redirect;

    /**
     * Possibly null Model to render.
     */
    public final Model model;

    /**
     * How this page was produced.
     */
    public final AnnotatedCallTree production = Logs.of().annotatedCallTree();

    /**
     * Where we log to.
     */
    public final Log log = Logs.of();

    /**
     * Use this in place of null.
     */
    public static PageResponse NULL = new PageResponse();

    /**
     * Just for null above.
     */
    private PageResponse() {
        this.request = PageRequest.NULL;
        this.model   = Model.NULL;
        redirect     = null;
    }

    /**
     * Normal constructor -- use a factory.
     */
    private PageResponse(PageRequest request, Model model) {
        this.request = notNull(request);
        this.model = notNull(model);
        redirect   = null;
    }

    /**
     * Redirect constructor -- use a factory.
     */
    private PageResponse(PageRequest request,URIObject redirect) {
        this.request = notNull(request);
        this.model    = null;
        this.redirect = notNull(redirect);
        
    }

    /**
     * For error pages.
     */
    public static PageResponse of(PageRequest request,Throwable throwable) {
        return of(request,AnnotatedStackTraceModel.throwable(throwable));
    }

    /**
     * For a page that is just an array of bytes.
     */
    public static PageResponse of(PageRequest request,byte[] bytes,ContentType type) {
        return of(request,ByteArrayModel.bytesType(bytes,type));
    }

    public static PageResponse of(PageRequest request,Model model) {
        return new PageResponse(request,model);
    }

    public static PageResponse newRedirect(PageRequest request,URIObject dest) {
        return new PageResponse(request,dest);
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
