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
     * Possibly null URI to redirect to.
     */
    public final URI redirect;

    /**
     * Possibly null Model to render.
     */
    public final Model model;

    public final Log log;

    private PageResponse(Model model, Log log) {
        this.model = notNull(model);
        redirect   = null;
        this.log = notNull(log);
    }

    private PageResponse(URI redirect, Log log) {
        this.model    = null;
        this.redirect = notNull(redirect);
        this.log = notNull(log);
    }

    public static PageResponse of(Throwable throwable, Log log) {
        return of(AnnotatedStackTraceModel.throwable(throwable,log),log);
    }

    public static PageResponse of(byte[] bytes,ContentType type, Log log) {
        return of(ByteArrayModel.bytesType(bytes,type),log);
    }

    public static PageResponse of(Model model, Log log) {
        return new PageResponse(model,log);
    }

    public static PageResponse newRedirect(URI dest, Log log) {
        return new PageResponse(dest,log);
    }


}
