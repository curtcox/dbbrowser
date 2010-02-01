package com.cve.web.db;

import com.cve.model.db.SelectResults;
import com.cve.io.db.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.web.ClientInfo;
import com.cve.web.ClassMapModelHtmlRenderer;
import com.cve.web.CompositeModelHtmlRenderer;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.db.render.SelectResultsRenderer;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.cve.web.db.servers.ServerModelHtmlRenderers;
import com.google.common.collect.Maps;
import java.util.Map;
/**
 * Renderers for database pages.
 */
public final class DatabaseModelHtmlRenderers implements ModelHtmlRenderer {

    final ModelHtmlRenderer renderer;

    private DatabaseModelHtmlRenderers(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(TablesPage.class,                 PageDecorator.of(TablesPageRenderer.of()));
        map.put(TablesSearchPage.class,           PageDecorator.of(TablesSearchPageRenderer.of()));
        map.put(SelectResults.class,              PageDecorator.of(SelectResultsRenderer.of(serversStore,managedFunction)));
        map.put(FreeFormQueryModel.class,         PageDecorator.of(FreeFormQueryRenderer.of(db,hintsStore)));

        renderer = CompositeModelHtmlRenderer.of(
            ClassMapModelHtmlRenderer.of(map),
            ServerModelHtmlRenderers.of(managedFunction, serversStore)
        );
    }

    public static DatabaseModelHtmlRenderers of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        return new DatabaseModelHtmlRenderers(db,serversStore,hintsStore,managedFunction);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }

}
