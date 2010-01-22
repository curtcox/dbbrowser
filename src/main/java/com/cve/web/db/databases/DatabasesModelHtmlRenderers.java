package com.cve.web.db.databases;

import com.cve.log.Log;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import static com.cve.util.Check.notNull;

/**
 * Renderers for database pages.
 */
public final class DatabasesModelHtmlRenderers {

    final Log log;

    private DatabasesModelHtmlRenderers(Log log) {
        this.log = notNull(log);
    }

    public ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(DatabaseContentsSearchPage.class, PageDecorator.of(DatabaseContentsSearchPageRenderer.of(log)));
        map.put(DatabasesPage.class,              PageDecorator.of(new DatabasesPageRenderer()));
        map.put(DatabasesSearchPage.class,        PageDecorator.of(DatabasesSearchPageRenderer.of(log)));
        return ImmutableMap.copyOf(map);
    }

}
