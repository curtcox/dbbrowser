package com.cve.web.management;

import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.handlers.ResourceHandler;
import com.cve.ui.HTMLTags;
import com.cve.web.*;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.lang.AnnotatedClass;
import com.cve.lang.AnnotatedStackTrace;
import com.cve.lang.AnnotatedStackTraceElement;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import java.net.URI;

/**
 * For rendering throwables to HTML.
 * @author curt
 */
public final class AnnotatedStackTraceRenderer
    implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final HTMLTags tags;

    String tr(String s) { return tags.tr(s); }
    String th(String s) { return tags.th(s); }
    String td(String s) { return tags.td(s); }
    String body(String s) { return tags.body(s); }
    String html(String s) { return tags.html(s); }
    String borderTable(String s) { return tags.borderTable(s); }

    private AnnotatedStackTraceRenderer() {
        
        tags = HTMLTags.of();
    }

    public static AnnotatedStackTraceRenderer of() {
        return new AnnotatedStackTraceRenderer();
    }
    
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
            String header = tr(th("class") + th("file") + th("method") + th("arguments") + th("line") + th("source"));
            table.append(header);

            ImmutableList<AnnotatedStackTraceElement> elements = trace.elements;
            for (int i=0; i<elements.size(); i++) {
                AnnotatedStackTraceElement e     = elements.get(i);
                AnnotatedStackTraceElement next  = (i < elements.size() - 1 ) ? elements.get(i + 1) : null;
                Object[] args = trace.args.get(next);
                table.append(row(e,args));
            }
            out.append(borderTable(table.toString()));
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
    String row(AnnotatedStackTraceElement e, Object[] args) {
        if (args==null) {
            args = new Object[0];
        }
        AnnotatedClass c = e.clazz;
        String className = c.clazz.getName();
        String  fileName = c.file.toString();
        int line = e.line;
        return tr(
            td(className) +
            td(linkToSource(className,fileName).toString()) +
            td(e.executable.getName()) +
            td(argsCell(args)) +
            td("" + line) +
            td(e.source)
        );
    }

    String argsCell(Object[] args) {
        StringBuilder out = new StringBuilder();
        for (Object arg : args) {
            String label  = "" + arg;
            Object target = arg;
            out.append(ObjectLink.of().to(label,target) + " ");
        }
        return out.toString();
    }

    Link linkToSource(String className, String fileName) {
        Label text = Label.of(fileName);
        String classFileName = className.replace(".", "/") + ".java";
        URI target = URIs.of(ResourceHandler.PREFIX + classFileName);
        return Link.textTarget(text, target);
    }
}
