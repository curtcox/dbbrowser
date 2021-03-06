package com.cve.web.core;

import com.cve.ui.HTMLTags;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableRow;
import com.cve.ui.UITable;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
/**
 * This just dumps diagnostic information about the given request.
 */
public final class RequestDumpServlet {

    private final Log log = Logs.of();

    private final HTMLTags tags;

    private RequestDumpServlet() {
        
        tags = HTMLTags.of();
    }

    public static RequestDumpServlet of() {
        return new RequestDumpServlet();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");
        pw.print(tags.html(tags.body(tableOf(request))));
        pw.close();
    }

    String tableOf(HttpServletRequest r) {
        List<UITableRow> rows = Lists.newArrayList();
        add(rows,"Description","Value"                    ) ;
        add(rows,"Auth Type",            r.getAuthType() );
        add(rows,"Character Encoding",   r.getCharacterEncoding()   );
        add(rows,"Content Type",         r.getContentType()        );
        add(rows,"Context Path",         r.getContextPath()         );
        add(rows,"Local Addr",           r.getLocalAddr()           );
        add(rows,"Local Name",           r.getLocalName()           );
        add(rows,"Method",               r.getMethod()              );
        add(rows,"Path Info",            r.getPathInfo()            );
        add(rows,"Path Translated",      r.getPathTranslated()      );
        add(rows,"Protocol",             r.getProtocol()            );
        add(rows,"Query String",         r.getQueryString()         );
        add(rows,"Remote Addr",          r.getRemoteAddr()          );
        add(rows,"Remote Host",          r.getRemoteHost()          );
        add(rows,"Remote User",          r.getRemoteUser()          );
        add(rows,"Request URI",          r.getRequestURI()          );
        add(rows,"Requested Session Id", r.getRequestedSessionId()  );
        add(rows,"Scheme",               r.getScheme()              );
        add(rows,"Server Name",          r.getServerName()          );
        add(rows,"Servlet Path",         r.getServletPath()         );
        add(rows,"Content Length",       r.getContentLength()  );
        add(rows,"Local Port",           r.getLocalPort()      );
        add(rows,"Locale",               r.getLocale()         );
        add(rows,"Remote Port",          r.getRemotePort()     );
        add(rows,"Request URL",          r.getRequestURL()     );
        add(rows,"Server Port",          r.getServerPort()     );
        add(rows,"is Requested Session Id From Cookie", r.isRequestedSessionIdFromCookie());
        add(rows,"is Requested Session Id Valid",       r.isRequestedSessionIdValid());
        add(rows,"is Secure",            r.isSecure() ) ;
        Cookie[] cookies = r.getCookies();
        add(rows,"Cookies",              cookies==null ? null : Arrays.asList(r.getCookies()));
        Map parameters = new HashMap();
        parameters.putAll(r.getParameterMap());
        add(rows,"Parameter Map",        parameters);
        add(rows,"Description", "Value");
        
        return UITable.of(rows).toString();
    }

    void add(List<UITableRow> rows, String key, Object value) {
        rows.add(UITableRow.of(detail(key),detail("" + value)));
    }
    
    UITableDetail detail(String key) {
        return UITableDetail.of(key);
    }
}
