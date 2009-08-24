package com.cve.web;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Defines the mapping we use for the entire application.
 * @author curt
 */
final class ModelHtmlRendererMap {

    static final ImmutableMap<Class,ModelHtmlRenderer> RENDERERS = load();

    private static ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(ExitPage.class,         PageDecorator.of(new ExitPageRenderer()));
        map.put(StringModel.class,      PageDecorator.of(new StringModelRenderer()));
        return ImmutableMap.copyOf(map);
    }


}
