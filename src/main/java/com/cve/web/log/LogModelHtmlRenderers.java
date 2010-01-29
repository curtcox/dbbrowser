package com.cve.web.log;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.ClassMapModelHtmlRenderer;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for log pages.
 */
public final class LogModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final ModelHtmlRenderer renderer;

    private LogModelHtmlRenderers() {
        
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(AnnotatedStackTraceModel.class, AnnotatedStackTraceRenderer.of());
        map.put(ObjectModel.class,  ObjectModelRenderer.of());
        map.put(RequestModel.class, RequestModelRenderer.of());
        map.put(RequestIndexModel.class, RequestIndexRenderer.of());
        renderer = ClassMapModelHtmlRenderer.of(map);
    }

    public static LogModelHtmlRenderers of() {
        return new LogModelHtmlRenderers();
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }

}
