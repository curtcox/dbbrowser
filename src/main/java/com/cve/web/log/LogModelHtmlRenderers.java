package com.cve.web.log;

import com.cve.log.Log;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for log pages.
 */
public final class LogModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log;

    public static final ImmutableMap<Class,ModelHtmlRenderer> RENDERERS = load();

    public static ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(AnnotatedStackTraceModel.class, AnnotatedStackTraceRenderer.of(log));
        map.put(ObjectModel.class, new ObjectModelRenderer());
        return ImmutableMap.copyOf(map);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
