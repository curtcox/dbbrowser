package com.cve.html;

import com.cve.log.Log;
import com.cve.util.Replace;
import com.cve.web.DebugHandler;
import com.cve.web.PageRequest;
import java.net.URI;
import static com.cve.util.Check.notNull;
/**
 * A typesafe string for HMTL, plus some string-based static methods to make
 * constructing it easier.  Also, use of this class enables some debugging
 * functionality.
 */
public final class HTMLTags {

    final Log log;

    private HTMLTags(Log log) {
        this.log = notNull(log);
    }

    public static HTMLTags of(Log log) {
        return new HTMLTags(log);
    }

    public String  html(String s) { return "<html>" + s + "</html>"; }
    public String  head(String s) { return "<head>" + s + "</head>"; }
    public String title(String s) { return "<title>" + s + "</title>"; }
    public String  base(URI href) { return "<base href=" + q(href) + "/>"; }
    public String  body(String s) { return "<body>" + s + "</body>"; }
    public String    li(String s) { return debug("<li>",s,"</li>"); }
    public String    ol(String s) { return debug("<ol>",s ,"</ol>"); }
    public String    h1(String s) { return debug("<h1>",s,"</h1>"); }
    public String    h2(String s) { return debug("<h2>",s,"</h2>"); }
    public String    td(String s) { return debug("<td>",s,"</td>"); }
    public String    th(String s) { return debug("<th>",s,"</th>"); }
    public String    tr(String s) { return debug("<tr>",s,"</tr>\r"); }
    public String     b(String s) { return debug("<b>",s,"</b>\r"); }
    public String     i(String s) { return debug("<i>",s,"</i>\r"); }
    public String   pre(String s) { return debug("<pre>\r",s,"\r</pre>\r"); }
    public String table(String s) {
        if (DebugHandler.isOn()) {
            return debug("<table border>",s,"</table>\r");
        }
        return debug("<table>",s,"</table>\r");
    }
    public String borderTable(String s) { return debug("<table border>",s,"</table>\r"); }

    public String    td(String s, CSS css, int width, int height) {
        return debug("<td class=" + q(css.toString()) + " colspan=" + q(width) + "rowspan=" + q(height)+ ">",s,"</td>");
    }
    public String    td(String s, int width) { return debug("<td colspan=" + q(width) + ">",s,"</td>"); }
    public String    td(String s, CSS css)   { return debug("<td class=" + q(css.toString()) + ">",s,"</td>"); }
    public String    td(String s, CSS... css)   { return debug("<td class=" + q(spaces(css)) + ">",s,"</td>"); }
    public String    th(String s, CSS css)   { return debug("<th class=" + q(css.toString()) + ">",s,"</th>"); }
    public String    tr(String s, CSS css)   { return debug("<tr class=" + q(css.toString()) + ">",s,"</tr>\r"); }

    /**
     * Image for the given URI.
     * "Alt text is an alternative, not a tooltip"
     * See http://www.456bereastreet.com/archive/200604/alt_text_is_an_alternative_not_a_tooltip/
     */
    public static String img(String title, URI uri) {
        return Replace.bracketQuote("<img alt=[" + title + "] title=[" + title + "] src=[" + uri + "]>");
    }

    /**
     <form action="form_handler.php" method="get">
         User Name: <input name="user" type="text" />
         <input type="submit" />
     </form>
     */
    public String form(String action, PageRequest.Method method, String body) {
         return 
             "<form action=\"" + action + "\" method=\"" + method +"\">" +
                 body +
             "</form>";
    }

    public String q(Object o) { return "\"" + o + "\"";  }
    public String spaces(CSS... csses) {
        StringBuilder out = new StringBuilder();
        for (CSS css : csses) {
            out.append(css + " ");
        }
        return out.toString();
    }

    private String debug(String open, String body, String close) {
        return open + body + DebugHandler.debugLink(log) + close;
    }
}