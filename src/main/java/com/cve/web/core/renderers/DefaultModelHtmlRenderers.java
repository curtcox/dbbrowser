package com.cve.web.core.renderers;

import com.cve.web.core.ModelHtmlRenderer;
import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.ui.UIElement;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.alt.AltModelHtmlRenderers;
import com.cve.web.db.DatabaseModelHtmlRenderers;
import com.cve.web.management.ManagementModelHtmlRenderers;

/**
 * Renders models into HTML, JPG, PNG, etc...
 * Generally, this is the top-level renderer and the only one directly
 * given to the router servlet.
 * @author curt
 */
public final class DefaultModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log = Logs.of();

    /**
     * The composite model we delegate to.
     */
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
    public UIElement render(Model model, ClientInfo client) {
        log.args(model,client);
        if (model instanceof ModelHtmlRenderer) {
            ModelHtmlRenderer selfRendering = (ModelHtmlRenderer) model;
            return selfRendering.render(model, client);
        }
        return renderer.render(model, client);
    }
}
