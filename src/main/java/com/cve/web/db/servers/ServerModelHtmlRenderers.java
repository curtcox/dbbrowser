package com.cve.web.db.servers;

import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for database pages.
 */
public final class ServerModelHtmlRenderers {

    public static final ImmutableMap<Class,ModelHtmlRenderer> RENDERERS = load();

    public static ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(ServersPage.class,                PageDecorator.of(new ServersPageRenderer()));
        map.put(ServersSearchPage.class,          PageDecorator.of(new ServersSearchPageRenderer()));
        map.put(AddServerPage.class,              PageDecorator.of(new AddServerPageRenderer()));
        return ImmutableMap.copyOf(map);
    }

}
