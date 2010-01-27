package com.cve.web;

import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.web.alt.AltModelHtmlRenderers;
import com.cve.web.db.DatabaseModelHtmlRenderers;
import com.cve.web.log.LogModelHtmlRenderers;
import static com.cve.util.Check.notNull;

/**
 * Renders models into HTML, JPG, PNG, etc...
 * @author curt
 */
public final class DefaultModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log;

    final ModelHtmlRenderer renderer;

    private DefaultModelHtmlRenderers(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction,Log log)
    {
        this.log = notNull(log);
        renderer = CompositeModelHtmlRenderer.of(log,
            DatabaseModelHtmlRenderers.of(db,serversStore,hintsStore,managedFunction,log),
            LogModelHtmlRenderers.of(log),
            AltModelHtmlRenderers.of(log),
            GlobalHtmlRenderers.of(log)
        );
    }

    public static DefaultModelHtmlRenderers of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction,Log log)
    {
        return new DefaultModelHtmlRenderers(db,serversStore,hintsStore,managedFunction,log);
    }
    

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }
}
