package com.cve.web.db;

import com.cve.model.db.SelectResults;
import com.cve.io.db.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.web.db.render.SelectResultsRenderer;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
import com.cve.web.db.databases.DatabasesModelHtmlRenderers;
import com.cve.web.db.servers.ServerModelHtmlRenderers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for database pages.
 */
public final class DatabaseModelHtmlRenderers {

    final DBMetaData.Factory db;
    final DBServersStore serversStore;
    final DBHintsStore hintsStore;
    final ManagedFunction.Factory managedFunction;

    private DatabaseModelHtmlRenderers(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        this.db = db;
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
        this.managedFunction = managedFunction;
    }

    public static ImmutableMap<Class,ModelHtmlRenderer> of(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new DatabaseModelHtmlRenderers(db,serversStore,hintsStore,managedFunction).get();
    }

    private ImmutableMap<Class,ModelHtmlRenderer> get() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(TablesPage.class,                 PageDecorator.of(new TablesPageRenderer()));
        map.put(TablesSearchPage.class,           PageDecorator.of(new TablesSearchPageRenderer()));
        map.put(SelectResults.class,              PageDecorator.of(SelectResultsRenderer.of(serversStore,managedFunction)));
        map.put(FreeFormQueryModel.class,         PageDecorator.of(FreeFormQueryRenderer.of(db,hintsStore)));
        map.putAll(ServerModelHtmlRenderers.RENDERERS);
        map.putAll(DatabasesModelHtmlRenderers.RENDERERS);
        return ImmutableMap.copyOf(map);
    }

}
