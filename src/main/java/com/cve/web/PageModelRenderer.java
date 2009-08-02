package com.cve.web;

import com.cve.db.SelectResults;
import com.cve.db.render.SelectResultsRenderer;
import com.cve.util.Check;
import com.cve.web.db.DatabasesPage;
import com.cve.web.db.DatabasesPageRenderer;
import com.cve.web.db.ServersPage;
import com.cve.web.db.ServersPageRenderer;
import com.cve.web.db.TablesPage;
import com.cve.web.db.TablesPageRenderer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 *
 * @author curt
 */
final class PageModelRenderer implements ModelRenderer {

    private final ImmutableMap<Class,ModelRenderer> RENDERERS = loadRenderers();

    private final ImmutableMap<Class,ModelRenderer> loadRenderers() {
        Map<Class,ModelRenderer> map = Maps.newHashMap();
        map.put(ExitPage.class,         PageDecorator.of(new ExitPageRenderer()));
        map.put(ServersPage.class,      PageDecorator.of(new ServersPageRenderer()));
        map.put(DatabasesPage.class,    PageDecorator.of(new DatabasesPageRenderer()));
        map.put(TablesPage.class,       PageDecorator.of(new TablesPageRenderer()));
        map.put(SelectResults.class,    PageDecorator.of(new SelectResultsRenderer()));
        map.put(StringModel.class,      PageDecorator.of(new StringModelRenderer()));
        map.put(ThrowableModel.class,   new ThrowableModelRenderer());
        return ImmutableMap.copyOf(map);
    }

    public PageModelRenderer() {}

    public Object render(Model model, ClientInfo client) {
        Check.notNull(model);
        Class toRender = model.getClass();
        ModelRenderer renderer = RENDERERS.get(model.getClass());
        if (renderer==null) {
            String message = "No renderer for " + toRender.toString();
            throw new IllegalArgumentException(message);
        }
        return renderer.render(model,client);
    }

}
