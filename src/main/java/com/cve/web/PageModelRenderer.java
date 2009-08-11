package com.cve.web;

import com.cve.web.db.TablesPage;
import com.cve.web.db.TablesPageRenderer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 *
 * @author curt
 */
final class PageModelRenderer {

    static final ImmutableMap<Class,ModelRenderer> RENDERERS = load();

    private static ImmutableMap<Class,ModelRenderer> load() {
        Map<Class,ModelRenderer> map = Maps.newHashMap();
        map.put(ExitPage.class,         PageDecorator.of(new ExitPageRenderer()));
        map.put(TablesPage.class,       PageDecorator.of(new TablesPageRenderer()));
        map.put(StringModel.class,      PageDecorator.of(new StringModelRenderer()));
        map.put(ThrowableModel.class,   new ThrowableModelRenderer());
        return ImmutableMap.copyOf(map);
    }


}
