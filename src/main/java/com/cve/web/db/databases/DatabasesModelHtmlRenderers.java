package com.cve.web.db.databases;

import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for database pages.
 */
public final class DatabasesModelHtmlRenderers {

    public static final ImmutableMap<Class,ModelHtmlRenderer> RENDERERS = load();

    public static ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(DatabaseContentsSearchPage.class, PageDecorator.of(new DatabaseContentsSearchPageRenderer()));
        map.put(DatabasesPage.class,              PageDecorator.of(new DatabasesPageRenderer()));
        map.put(DatabasesSearchPage.class,        PageDecorator.of(new DatabasesSearchPageRenderer()));
        return ImmutableMap.copyOf(map);
    }

}
