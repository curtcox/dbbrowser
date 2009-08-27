package com.cve.web.alt;

import com.cve.web.ModelHtmlRenderer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for alternate views of result sets.
 * @author Curt
 */
public final class AltModelHtmlRenderers {

    public static final ImmutableMap<Class,ModelHtmlRenderer> RENDERERS = load();

    public static ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(CSVModel.class, new CSVModelRenderer());
        return ImmutableMap.copyOf(map);
    }
}
