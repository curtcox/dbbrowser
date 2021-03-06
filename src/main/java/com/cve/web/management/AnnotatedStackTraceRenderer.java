package com.cve.web.management;

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
import com.cve.lang.URIObject;
import com.cve.ui.UIComposite;
import com.cve.ui.UIElement;
import com.cve.ui.UILabel;
import com.cve.ui.UILink;
import com.cve.ui.UITableBuilder;
import com.cve.ui.UITableCell;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableHeader;
import com.cve.ui.UITableRow;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * For rendering throwables to HTML.
 * @author curt
 */
public final class AnnotatedStackTraceRenderer
    implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final HTMLTags tags;

    UITableRow    tr(UITableCell... cells)  { return UITableRow.of(cells); }
    UITableHeader th(String s)  { return UITableHeader.of(s); }
    UITableDetail td(String s)  { return UITableDetail.of(s); }
    UITableDetail td(UIElement e)  { return UITableDetail.of(e); }

    private AnnotatedStackTraceRenderer() {
        
        tags = HTMLTags.of();
    }

    public static AnnotatedStackTraceRenderer of() {
        return new AnnotatedStackTraceRenderer();
    }
    
    @Override
    public UIElement render(Model model, ClientInfo client) {
        AnnotatedStackTraceModel objectModel = (AnnotatedStackTraceModel) model;
        AnnotatedStackTrace t = objectModel.trace;
        return render(t);
    }

    UIElement render(AnnotatedStackTrace trace) {
        UITableBuilder out = UITableBuilder.of();
        while (trace!=null) {
            Throwable throwable = trace.throwable;
            out.add(tr(td("<b>" + throwable.getClass().getName() + "</b> " + throwable.getMessage())));
            StringBuilder table = new StringBuilder();
            UITableRow header = tr(th("class"),th("file"),th("method"),th("arguments"),th("line"),th("source"));
            table.append(header);

            ImmutableList<AnnotatedStackTraceElement> elements = trace.elements;
            for (int i=0; i<elements.size(); i++) {
                AnnotatedStackTraceElement e     = elements.get(i);
                AnnotatedStackTraceElement next  = (i < elements.size() - 1 ) ? elements.get(i + 1) : null;
                Object[] args = trace.args.get(next);
                table.append(row(e,args));
            }
            trace = trace.cause;
            if (trace!=null) {
                out.add(tr(td("Caused by")));
            }
            table.append(header);
        }
        return out.build();
    }

    /**
     * Return the HTML for a stack trace element.
     */
    UITableRow row(AnnotatedStackTraceElement e, Object[] args) {
        if (args==null) {
            args = new Object[0];
        }
        AnnotatedClass c = e.clazz;
        String className = c.clazz.getName();
        String  fileName = c.file.toString();
        int line = e.line;
        return UITableRow.of(
            td(className),
            td(linkToSource(className,fileName).toString()),
            td(e.executable.getName()),
            td(argsCell(args)),
            td("" + line),
            td(e.source.toString())
        );
    }

    UIElement argsCell(Object[] args) {
        List<UIElement> out = Lists.newArrayList();
        for (Object arg : args) {
            String label  = "" + arg;
            Object target = arg;
            out.add(UILink.to(label,target));
        }
        return UIComposite.of(out);
    }

    Link linkToSource(String className, String fileName) {
        Label text = Label.of(fileName);
        String classFileName = className.replace(".", "/") + ".java";
        URIObject target = URIs.of(ResourceHandler.PREFIX + classFileName);
        return Link.textTarget(text, target);
    }
}
