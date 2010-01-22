package com.cve.web.db.servers;

import com.cve.log.Log;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import static com.cve.util.Check.notNull;

/**
 * Renderers for database pages.
 */
public final class ServerModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log;

    final ModelHtmlRenderer renderer;

    private ServerModelHtmlRenderers(Log log) {
        this.log = notNull(log);
    }

    public static final ImmutableMap<Class,ModelHtmlRenderer> RENDERERS = load();

    public ImmutableMap<Class,ModelHtmlRenderer> load() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(ServersPage.class,                PageDecorator.of(ServersPageRenderer.of(log)));
        map.put(ServersSearchPage.class,          PageDecorator.of(ServersSearchPageRenderer.of(log)));
        map.put(AddServerPage.class,              PageDecorator.of(AddServerPageRenderer.of(log)));
        return ImmutableMap.copyOf(map);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }

}
