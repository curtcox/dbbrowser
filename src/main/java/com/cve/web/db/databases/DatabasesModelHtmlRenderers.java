package com.cve.web.db.databases;

import com.cve.log.Log;
import com.cve.web.ClassMapModelHtmlRenderer;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.google.common.collect.Maps;
import java.util.Map;
import static com.cve.util.Check.notNull;

/**
 * Renderers for database pages.
 */
public final class DatabasesModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log;

    final ModelHtmlRenderer renderer;

    private DatabasesModelHtmlRenderers(Log log) {
        this.log = notNull(log);
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(DatabaseContentsSearchPage.class, PageDecorator.of(DatabaseContentsSearchPageRenderer.of(log)));
        map.put(DatabasesPage.class,              PageDecorator.of(DatabasesPageRenderer.of(log)));
        map.put(DatabasesSearchPage.class,        PageDecorator.of(DatabasesSearchPageRenderer.of(log)));
        renderer = ClassMapModelHtmlRenderer.of(map, log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }

}
