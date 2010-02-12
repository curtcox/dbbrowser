package com.cve.web;

import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.web.alt.AltModelHtmlRenderers;
import com.cve.web.db.DatabaseModelHtmlRenderers;
import com.cve.web.management.ManagementModelHtmlRenderers;

/**
 * Renders models into HTML, JPG, PNG, etc...
 * @author curt
 */
public final class DefaultModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final ModelHtmlRenderer renderer;

    private DefaultModelHtmlRenderers(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        
        renderer = CompositeModelHtmlRenderer.of(
            DatabaseModelHtmlRenderers.of(db,serversStore,hintsStore,managedFunction),
            ManagementModelHtmlRenderers.of(),
            AltModelHtmlRenderers.of(),
            GlobalHtmlRenderers.of()
        );
    }

    public static DefaultModelHtmlRenderers of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        return new DefaultModelHtmlRenderers(db,serversStore,hintsStore,managedFunction);
    }
    

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }
}
