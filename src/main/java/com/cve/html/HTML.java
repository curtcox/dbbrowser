package com.cve.html;

import com.cve.web.PageRequest;
import java.net.URI;
import static com.cve.util.Check.notNull;
/**
 * A typesafe string for HMTL, plus some string-based static methods to make
 * constructing it easier.  Also, use of this class enables some debugging
 * functionality.
 */
public final class HTML {

    private final String html;

    private HTML(String html) {
        this.html = notNull(html);
    }

    public static HTML of(String html) {
        return new HTML(html);
    }

    @Override
    public String toString() { return html; }

    public static String  html(String s) { return "<html>" + s + "</html>"; }
    public static String  head(String s) { return "<head>" + s + "</head>"; }
    public static String title(String s) { return "<title>" + s + "</title>"; }
    public static String  base(URI href) { return "<base href=" + q(href) + "/>"; }
    public static String  body(String s) { return "<body>" + s + "</body>"; }
    public static String    li(String s) { return debug("<li>",s,"</li>"); }
    public static String    ol(String s) { return debug("<ol>",s ,"</ol>"); }
    public static String    h1(String s) { return debug("<h1>",s,"</h1>"); }
    public static String    h2(String s) { return debug("<h2>",s,"</h2>"); }
    public static String    td(String s) { return debug("<td>",s,"</td>"); }
    public static String    th(String s) { return debug("<th>",s,"</th>"); }
    public static String    tr(String s) { return debug("<tr>",s,"</tr>\r"); }
    public static String   pre(String s) { return debug("<pre>\r",s,"\r</pre>\r"); }
    public static String table(String s) { return debug("<table>",s,"</table>\r"); }
    public static String borderTable(String s) { return debug("<table border>",s,"</table>\r"); }

    public static String    td(String s, CSS css, int width, int height) {
        return debug("<td class=" + q(css.toString()) + " colspan=" + q(width) + "rowspan=" + q(height)+ ">",s,"</td>");
    }
    public static String    td(String s, int width) { return debug("<td colspan=" + q(width) + ">",s,"</td>"); }
    public static String    td(String s, CSS css)   { return debug("<td class=" + q(css.toString()) + ">",s,"</td>"); }
    public static String    td(String s, CSS... css)   { return debug("<td class=" + q(spaces(css)) + ">",s,"</td>"); }
    public static String    th(String s, CSS css)   { return debug("<th class=" + q(css.toString()) + ">",s,"</th>"); }
    public static String    tr(String s, CSS css)   { return debug("<tr class=" + q(css.toString()) + ">",s,"</tr>\r"); }

    /**
     <form action="form_handler.php" method="get">
         User Name: <input name="user" type="text" />
         <input type="submit" />
     </form>
     */
    public static String form(String action, PageRequest.Method method, String body) {
         return 
             "<form action=\"" + action + "\" method=\"" + method +"\">" +
                 body +
             "</form>";
    }

    public static String q(Object o) { return "\"" + o + "\"";  }
    public static String spaces(CSS... csses) {
        StringBuilder out = new StringBuilder();
        for (CSS css : csses) {
            out.append(css + " ");
        }
        return out.toString();
    }

    private static String debug(String open, String body, String close) {
        return open + body + close;
    }
}
