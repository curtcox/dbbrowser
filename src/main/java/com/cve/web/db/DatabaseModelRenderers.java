package com.cve.web.db;

import com.cve.db.SelectResults;
import com.cve.db.render.SelectResultsRenderer;
import com.cve.web.ModelRenderer;
import com.cve.web.PageDecorator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for database pages.
 */
public final class DatabaseModelRenderers {

    public static final ImmutableMap<Class,ModelRenderer> RENDERERS = load();

    public static ImmutableMap<Class,ModelRenderer> load() {
        Map<Class,ModelRenderer> map = Maps.newHashMap();
        map.put(ServersPage.class,      PageDecorator.of(new ServersPageRenderer()));
        map.put(DatabasesPage.class,    PageDecorator.of(new DatabasesPageRenderer()));
        map.put(TablesPage.class,       PageDecorator.of(new TablesPageRenderer()));
        map.put(SelectResults.class,    PageDecorator.of(new SelectResultsRenderer()));
        map.put(AddServerPage.class,    PageDecorator.of(new AddServerPageRenderer()));
        return ImmutableMap.copyOf(map);
    }

}
