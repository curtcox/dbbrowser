package com.cve.web.db.servers;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.web.ClientInfo;
import com.cve.web.ClassMapModelHtmlRenderer;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for database pages.
 */
public final class ServerModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final ModelHtmlRenderer renderer;

    private ServerModelHtmlRenderers(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(ServersPage.class,                PageDecorator.of(ServersPageRenderer.of()));
        map.put(ServersSearchPage.class,          PageDecorator.of(ServersSearchPageRenderer.of()));
        map.put(AddServerPage.class,              PageDecorator.of(AddServerPageRenderer.of(managedFunction,serversStore)));
        renderer = ClassMapModelHtmlRenderer.of(map);
    }

    public static ServerModelHtmlRenderers of(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
         return new ServerModelHtmlRenderers(managedFunction, serversStore);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }

}
