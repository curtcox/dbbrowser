package com.cve.web.core.handlers;

import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For grouping {@link RequestHandler}S.
 * The handlers that match their specified prefixes will each be asked in turn
 * to service the request until one does.
 * @author Curt
 */
@Immutable
public final class PrefixMapRequestHandler implements RequestHandler {

    private final ImmutableMap<Pattern,RequestHandler> handlers;

    private PrefixMapRequestHandler(Map<Pattern,RequestHandler> handlers) {
        notNull(handlers);
        this.handlers = ImmutableMap.copyOf(handlers);
    }

    /**
     * Convenient method for one pair.
     */
    public static RequestHandler of(String prefix, RequestHandler handler) {
         Map<Pattern,RequestHandler> handlers = Maps.newHashMap();
         handlers.put(Pattern.compile(prefix), handler);
         return new PrefixMapRequestHandler(handlers);
    }

    /**
     * Convenient method for two pairs.
     */
    public static RequestHandler of(
         String prefix1, RequestHandler handler1,
         String prefix2, RequestHandler handler2)
    {
         Map<Pattern,RequestHandler> handlers = Maps.newHashMap();
         handlers.put(Pattern.compile(prefix1), handler1);
         handlers.put(Pattern.compile(prefix2), handler2);
         return new PrefixMapRequestHandler(handlers);
    }

    /**
     * Convenient method for three pairs.
     */
    public static RequestHandler of(
         String prefix1, RequestHandler handler1,
         String prefix2, RequestHandler handler2,
         String prefix3, RequestHandler handler3)
    {
         Map<Pattern,RequestHandler> handlers = Maps.newHashMap();
         handlers.put(Pattern.compile(prefix1), handler1);
         handlers.put(Pattern.compile(prefix2), handler2);
         handlers.put(Pattern.compile(prefix3), handler3);
         return new PrefixMapRequestHandler(handlers);
    }

    /**
     * Convenient method for four pairs.
     */
    public static RequestHandler of(
         String prefix1, RequestHandler handler1,
         String prefix2, RequestHandler handler2,
         String prefix3, RequestHandler handler3,
         String prefix4, RequestHandler handler4)
    {
         Map<Pattern,RequestHandler> handlers = Maps.newHashMap();
         handlers.put(Pattern.compile(prefix1), handler1);
         handlers.put(Pattern.compile(prefix2), handler2);
         handlers.put(Pattern.compile(prefix3), handler3);
         handlers.put(Pattern.compile(prefix4), handler4);
         return new PrefixMapRequestHandler(handlers);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        String uri = request.requestURI;
        for (Pattern pattern : handlers.keySet()) {
            if (pattern.matcher(uri).find()) {
                RequestHandler handler = handlers.get(pattern);
                PageResponse response = handler.produce(request);
                if (response!=null) {
                    return response;
                }
            }
        }
        return null;
    }

}
