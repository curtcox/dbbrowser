package com.cve.web.log;

import com.cve.log.Log;
import com.cve.web.ClassMapModelHtmlRenderer;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.google.common.collect.Maps;
import java.util.Map;
import static com.cve.util.Check.notNull;

/**
 * Renderers for log pages.
 */
public final class LogModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log;

    final ModelHtmlRenderer renderer;

    private LogModelHtmlRenderers(Log log) {
        this.log = notNull(log);
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(AnnotatedStackTraceModel.class, AnnotatedStackTraceRenderer.of(log));
        map.put(ObjectModel.class, ObjectModelRenderer.of(log));
        renderer = ClassMapModelHtmlRenderer.of(map,log);
    }

    public static LogModelHtmlRenderers of(Log log) {
        return new LogModelHtmlRenderers(log);
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }

}
