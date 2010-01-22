package com.cve.web.db;

import com.cve.model.db.SelectResults;
import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
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
import static com.cve.util.Check.notNull;
/**
 * Renderers for database pages.
 */
public final class DatabaseModelHtmlRenderers {

    final DBMetaData.Factory db;
    final DBServersStore serversStore;
    final DBHintsStore hintsStore;
    final ManagedFunction.Factory managedFunction;
    final Log log;

    private DatabaseModelHtmlRenderers(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction, Log log)
    {
        this.db = notNull(db);
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
        this.managedFunction = managedFunction;
        this.log = notNull(log);
    }

    public static ImmutableMap<Class,ModelHtmlRenderer> of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction, Log log)
    {
        return new DatabaseModelHtmlRenderers(db,serversStore,hintsStore,managedFunction,log).get();
    }

    private ImmutableMap<Class,ModelHtmlRenderer> get() {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(TablesPage.class,                 PageDecorator.of(TablesPageRenderer.of(log)));
        map.put(TablesSearchPage.class,           PageDecorator.of(TablesSearchPageRenderer.of(log)));
        map.put(SelectResults.class,              PageDecorator.of(SelectResultsRenderer.of(serversStore,managedFunction,log)));
        map.put(FreeFormQueryModel.class,         PageDecorator.of(FreeFormQueryRenderer.of(db,hintsStore,log)));
        map.putAll(ServerModelHtmlRenderers.RENDERERS);
        map.putAll(DatabasesModelHtmlRenderers.RENDERERS);
        return ImmutableMap.copyOf(map);
    }

}
