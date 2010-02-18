package com.cve.web.core.handlers;

import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For grouping {@link RequestHandler}S.
 * The handlers will each be asked in turn to service the request until one
 * does.
 * @author Curt
 */
@Immutable
public final class CompositeRequestHandler implements RequestHandler {

    private final ImmutableList<RequestHandler> handlers;

    private CompositeRequestHandler(RequestHandler... handlers) {
        notNull(handlers);
        this.handlers = ImmutableList.copyOf(Arrays.asList(handlers));
    }

    public static RequestHandler of(RequestHandler... handlers) {
         return new CompositeRequestHandler(handlers);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        for (RequestHandler handler : handlers) {
            PageResponse response = handler.produce(request);
            if (response!=null) {
                return response;
            }
        }
        return null;
    }

}
