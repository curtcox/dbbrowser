package com.cve.web.log;

import com.cve.web.ModelHtmlRenderer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for log pages.
 */
public final class LogModelHtmlRenderers {

    public static final ImmutableMap<Class,ModelHtmlRenderer> RENDERERS = load();

    public static ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(AnnotatedStackTraceModel.class, new AnnotatedStackTraceRenderer());
        map.put(ObjectModel.class, new ObjectModelRenderer());
        return ImmutableMap.copyOf(map);
    }

}
