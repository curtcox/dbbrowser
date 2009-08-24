package com.cve.web;

import com.cve.web.log.AnnotatedStackTraceModel;
import com.cve.log.Log;
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

    private PageResponse(Model model) {
        this.model = notNull(model);
        redirect   = null;
    }

    private PageResponse(URI redirect) {
        this.model    = null;
        this.redirect = notNull(redirect);
    }

    public static PageResponse of(Throwable throwable) {
        return of(AnnotatedStackTraceModel.throwable(throwable));
    }

    public static PageResponse of(byte[] bytes) {
        return of(new ByteArrayModel(bytes));
    }

    public static PageResponse of(Model model) {
        return new PageResponse(model);
    }

    public static PageResponse newRedirect(URI dest) {
        return new PageResponse(dest);
    }


}
