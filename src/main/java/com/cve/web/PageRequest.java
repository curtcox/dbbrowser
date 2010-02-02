
package com.cve.web;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletRequest;
import com.cve.util.Check;
import com.cve.util.Timestamp;
import com.cve.web.log.LogCodec;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.net.URI;
import java.util.Map;
import javax.servlet.http.Cookie;

/**
 * An immutable HTTP page request.
 * @author curt
 */
@Immutable
public final class PageRequest {

    /**
     * How a page is being requested -- GET or POST.
     */
    public enum Method {
        GET, POST;
    }

    /**
     * Unique id for a request.
     */
    @Immutable
    public static class ID implements Comparable<ID> {

        public final Timestamp timestamp;

        static final Log log = Logs.of();
        
        final LogCodec codec = LogCodec.of();

        static final ThreadLocal<ID> local = new ThreadLocal() {
            @Override protected ID initialValue() {
                 return new ID();
            }
        };

        private ID() {
            timestamp = Timestamp.of();
        }

        private ID(Timestamp timestamp) {
            this.timestamp = Check.notNull(timestamp);
        }

        /**
         * Start using a new request.
         * The thread local isn't enough.  Web servers will tend to use thread
         * pools, so thread doesn't really imply request.
         */
        static void next() {
            local.set(new ID());
        }

        /**
         * This will be different for every thread, but always the same when
         * called from the same thread, between calls to next.
         */
        public static ID of() {
            return local.get();
        }

        public static ID parse(String string) {
            log.args(string);
            long code = Long.parseLong(string,16);
            return new ID(Timestamp.of(code));
        }

        @Override
        public int compareTo(ID other) {
            return timestamp.compareTo(other.timestamp);
        }

        @Override
        public String toString() {
            return "id=" + timestamp;
        }

        public Link linkTo() {
            Label text = Label.of("" + timestamp.value);
            URI target = codec.encode(this);
            return Link.textTarget(text, target);
        }

        @Override
        public int hashCode() {
            return timestamp.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            ID other = (ID) o;
            return timestamp.equals(other.timestamp);
        }
    }

    /**
     * The part of the URL given to the server, but ending at the
     * question mark for the query string.  In this case:
     * http://host:port/requestURI?queryString
     * just /requestURI
     */
    public final String requestURI;

    /**
     * The part of the URL given to the server starting after the
     * question mark for the query string.  In this case:
     * http://host:port/requestURI?queryString
     * just queryString
     */
    public final String queryString;

    /**
     * Parameters either parsed from the query string or from form post
     * contents.
     * Often empty, but never null.
     */
    public final ImmutableMap<String,String> parameters;

    /**
     * The cookies for this request.
     */
    public final ImmutableList<Cookie> cookies;

    /**
     * The method (GET, POST) used for this request.
     */
    public final Method method;

    /**
     * Unique ID for this request.
     */
    public final ID id;

    private PageRequest(
        Method type, String requestURI, String queryString,
        ImmutableMap<String,String> parameters, ImmutableList<Cookie> cookies)
    {
        this.method      = Check.notNull(type);
        this.requestURI  = Check.notNull(requestURI);
        this.queryString = Check.notNull(queryString);
        this.parameters  = Check.notNull(parameters);
        this.cookies     = Check.notNull(cookies);
        id = new ID();
    }

    public static PageRequest path(String pathInfo)
    {
        ImmutableMap<String,String> parameters = ImmutableMap.of();
        ImmutableList<Cookie> cookies = ImmutableList.of();
        return new PageRequest(Method.GET,pathInfo,"",parameters,cookies);
    }

    public static PageRequest request(HttpServletRequest request) {
        Check.notNull(request);
        Method        method = Method.valueOf(request.getMethod());
        String    requestURI = notNull(request.getRequestURI());
        String   queryString = notNull(request.getQueryString());
        Cookie[] cookieArray = request.getCookies();
        ImmutableList<Cookie> cookies = ImmutableList.of();
        if (cookieArray!=null){
            cookies = ImmutableList.of(cookieArray);
        }
        return new PageRequest(method,requestURI,queryString,parameters(request),cookies);
    }

    private static ImmutableMap<String,String> parameters(HttpServletRequest request) {
        Map<String,String> map = Maps.newHashMap();
        Map<String,String[]> oldMap = request.getParameterMap();
        for (String key : oldMap.keySet()) {
            String[] values = oldMap.get(key);
            if (values!=null && values.length > 0 && values[0]!=null) {
                map.put(key, values[0]);
            }
        }
        return ImmutableMap.copyOf(map);
    }

    private static String notNull(String string) {
        return (string==null) ? "" : string;
    }

    public PageRequest withURI(String requestURI) {
        return new PageRequest(method,requestURI,queryString,parameters,cookies);
    }

    @Override
    public boolean equals(Object o) {
        PageRequest other = (PageRequest) o;
        return requestURI.equals(other.requestURI) &&
               queryString.equals(other.queryString) &&
               parameters.equals(other.parameters) &&
               cookies.equals(other.cookies) &&
               method.equals(other.method) &&
               id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return requestURI.hashCode()  ^
               queryString.hashCode() ^
               parameters.hashCode()  ^
               cookies.hashCode()     ^
               method.hashCode()      ^
               id.hashCode();
    }

    @Override
    public String toString() {
        return "<PageRequest>" +
                " requestURI=" + requestURI +
                " queryString=" + queryString +
                " parameters=" + parameters +
                " cookies=" + cookies +
                " method=" + method +
                " id=" + id +
               "</PageRequest>";
    }
}
