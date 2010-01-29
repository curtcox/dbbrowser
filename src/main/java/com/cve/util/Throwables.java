package com.cve.util;

import com.cve.html.HTMLTags;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.ResourceHandler;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.URI;

/**
 * For printing throwables.
 */
public final class Throwables {

    private final Log log = Logs.of();

    private final HTMLTags tags;

    private Throwables() {
        
        tags = HTMLTags.of();
    }

    public static Throwables of() {
        return new Throwables();
    }
    
    public String toString(Throwable t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        t.printStackTrace(writer);
        writer.close();
        return tags.pre(out.toString());
    }

    /**
     * For now, just display the throwable as tables.
     * Later, we need to make the rows into links to the source.
     */
    public String toHtml(Throwable t) {
        StringBuilder out = new StringBuilder();
        while (t!=null) {
            out.append("<b>" + t.getClass().getName() + "</b> " + t.getMessage());
            StringBuilder table = new StringBuilder();
            String header = tr(th("class") + th("file") + th("method") + th("line"));
            table.append(header);
            for (StackTraceElement e : t.getStackTrace()) {
                table.append(row(e));
}
            out.append(borderTable(table.toString()));
            t = t.getCause();
            if (t!=null) {
                out.append("Caused by");
            }
            table.append(header);
        }
        return html(body(out.toString()));
    }

    String tr(String s) { return tags.tr(s); }
    String th(String s) { return tags.th(s); }
    String td(String s) { return tags.th(s); }
    String body(String s) { return tags.body(s); }
    String html(String s) { return tags.html(s); }
    String borderTable(String s) { return tags.borderTable(s); }

    /**
     * Return the HTML for a stack trace element.
     */
    String row(StackTraceElement e) {
        String className = e.getClassName();
        String  fileName = e.getFileName();
        return tr(
            td(className) +
            td(linkToSource(className,fileName).toString()) +
            td(e.getMethodName()) +
            td("" + e.getLineNumber())
        );

    }

    Link linkToSource(String className, String fileName) {
        Label text = Label.of(fileName);
        String classFileName = className.replace(".", "/") + ".java";
        URI target = URIs.of(ResourceHandler.PREFIX + classFileName);
        return Link.textTarget(text, target);
    }
}
