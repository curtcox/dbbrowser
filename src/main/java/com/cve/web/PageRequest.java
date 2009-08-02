
package com.cve.web;

import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletRequest;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.servlet.http.Cookie;

/**
 * An immutable HTTP page request.
 * @author curt
 */
@Immutable
public final class PageRequest {

    public enum Method {
        GET, POST;
    }

    /**
     * The part of the URL given to the server, but ending at the
     * question mark for the query string.  In this case:
     * http://host:port/requestURI?queryString
     * just /requestURI
     */
    private final String requestURI;

    /**
     * The part of the URL given to the server starting after the
     * question mark for the query string.  In this case:
     * http://host:port/requestURI?queryString
     * just queryString
     */
    private final String queryString;

    /**
     * Parameters either parsed from the query string or from form post
     * contents.
     */
    private final ImmutableMap<String,String> parameters;

    /**
     * The cookies for this request.
     */
    private final ImmutableList<Cookie> cookies;

    /**
     * The method (GET, POST) used for this request.
     */
    private final Method method;

    private PageRequest(
        Method type, String requestURI, String queryString,
        ImmutableMap<String,String> parameters, ImmutableList<Cookie> cookies)
    {
        this.method      = Check.notNull(type);
        this.requestURI  = Check.notNull(requestURI);
        this.queryString = Check.notNull(queryString);
        this.parameters  = Check.notNull(parameters);
        this.cookies     = Check.notNull(cookies);
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
        map.putAll(request.getParameterMap());
        return ImmutableMap.copyOf(map);
    }

    private static String notNull(String string) {
        return (string==null) ? "" : string;
    }

    public String getRequestURI()  { return requestURI;  }
    public String getQueryString() { return queryString; }
    public Method getMethod()      { return method;      }
    
    /**
     * Often empty, but never null.
     */
    public ImmutableMap<String,String> getParameterMap() { return parameters; }
    public ImmutableList<Cookie>            getCookies() { return cookies; }

}
