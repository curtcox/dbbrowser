package com.cve.util;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.web.ResourceHandler;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import java.net.URI;
import static com.cve.html.HTML.*;
/**
 * For printing throwables.
 */
public final class Throwables {

    public static String toString(Throwable t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        t.printStackTrace(writer);
        writer.close();
        return pre(out.toString());
    }

    /**
     * For now, just display the throwable as tables.
     * Later, we need to make the rows into links to the source.
     */
    public static String toHtml(Throwable t) {
        StringBuilder out = new StringBuilder();
        while (t!=null) {
            out.append("<b>" + t.getClass().getName() + "</b> " + t.getMessage());
            StringBuilder table = new StringBuilder();
            String header = tr(th("class") + th("file") + th("method") + th("line"));
            table.append(header);
            for (StackTraceElement e : t.getStackTrace()) {
                table.append(row(e));
            }
            out.append(table(table.toString()));
            t = t.getCause();
            if (t!=null) {
                out.append("Caused by");
            }
            table.append(header);
        }
        return html(body(out.toString()));
    }

    /**
     * Return the HTML for a stack trace element.
     */
    static String row(StackTraceElement e) {
        String className = e.getClassName();
        String fileName = e.getFileName();
        return tr(
            td(className) +
            td(linkToSource(className,fileName).toString()) +
            td(e.getMethodName()) +
            td("" + e.getLineNumber())
        );

    }

    static Link linkToSource(String className, String fileName) {
        Label text = Label.of(fileName);
        String classFileName = className.replace(".", "/") + ".java";
        URI target = URIs.of(ResourceHandler.PREFIX + classFileName);
        return Link.textTarget(text, target);
    }
}
