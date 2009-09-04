package com.cve.web.db;

import com.cve.db.SelectResults;
import com.cve.db.render.SelectResultsRenderer;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for database pages.
 */
public final class DatabaseModelHtmlRenderers {

    public static final ImmutableMap<Class,ModelHtmlRenderer> RENDERERS = load();

    public static ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(ServersPage.class,        PageDecorator.of(new ServersPageRenderer()));
        map.put(ServersSearchPage.class,  PageDecorator.of(new ServersSearchPageRenderer()));
        map.put(DatabasesPage.class,      PageDecorator.of(new DatabasesPageRenderer()));
        map.put(TablesPage.class,         PageDecorator.of(new TablesPageRenderer()));
        map.put(SelectResults.class,      PageDecorator.of(new SelectResultsRenderer()));
        map.put(AddServerPage.class,      PageDecorator.of(new AddServerPageRenderer()));
        map.put(FreeFormQueryModel.class, PageDecorator.of(new FreeFormQueryRenderer()));
        return ImmutableMap.copyOf(map);
    }

}
