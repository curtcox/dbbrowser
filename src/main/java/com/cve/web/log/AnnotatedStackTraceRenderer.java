package com.cve.web.log;

import com.cve.web.*;
import com.cve.html.Label;
import com.cve.html.Link;
import static com.cve.html.HTML.*;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import java.net.URI;

/**
 * For rendering throwables to HTML.
 * @author curt
 */
public final class AnnotatedStackTraceRenderer
    implements ModelHtmlRenderer {

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        AnnotatedStackTraceModel objectModel = (AnnotatedStackTraceModel) model;
        AnnotatedStackTrace t = objectModel.trace;
        return HtmlPage.guts(render(t));
    }

    String render(AnnotatedStackTrace trace) {
        StringBuilder out = new StringBuilder();
        while (trace!=null) {
            Throwable throwable = trace.throwable;
            out.append("<b>" + throwable.getClass().getName() + "</b> " + throwable.getMessage());
            StringBuilder table = new StringBuilder();
            String header = tr(th("class") + th("file") + th("method") + th("arguments") + th("line"));
            table.append(header);

            ImmutableList<StackTraceElement> elements = trace.elements;
            for (int i=0; i<elements.size(); i++) {
                StackTraceElement e     = elements.get(i);
                StackTraceElement next  = (i < elements.size() - 1 ) ? elements.get(i + 1) : null;
                Object[] args = trace.args.get(next);
                table.append(row(e,args));
            }
            out.append(table(table.toString()));
            trace = trace.cause;
            if (trace!=null) {
                out.append("Caused by");
            }
            table.append(header);
        }
        return html(body(out.toString()));
    }

    /**
     * Return the HTML for a stack trace element.
     */
    static String row(StackTraceElement e, Object[] args) {
        if (args==null) {
            args = new Object[0];
        }
        String className = e.getClassName();
        String  fileName = e.getFileName();
        return tr(
            td(className) +
            td(linkToSource(className,fileName).toString()) +
            td(e.getMethodName()) +
            td(argsCell(args)) +
            td("" + e.getLineNumber())
        );
    }

    static String argsCell(Object[] args) {
        StringBuilder out = new StringBuilder();
        for (Object arg : args) {
            String label  = "" + arg;
            Object target = arg;
            out.append(ObjectLink.to(label,target) + " ");
        }
        return out.toString();
    }

    static Link linkToSource(String className, String fileName) {
        Label text = Label.of(fileName);
        String classFileName = className.replace(".", "/") + ".java";
        URI target = URIs.of(ResourceHandler.PREFIX + classFileName);
        return Link.textTarget(text, target);
    }
}
