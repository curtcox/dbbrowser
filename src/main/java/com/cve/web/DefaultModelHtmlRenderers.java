package com.cve.web;

import com.cve.io.db.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.web.alt.AltModelHtmlRenderers;
import com.cve.web.db.DatabaseModelHtmlRenderers;
import com.cve.web.log.LogModelHtmlRenderers;

/**
 * Renders models into HTML, JPG, PNG, etc...
 * @author curt
 */
public class DefaultModelHtmlRenderers {

    /**
     * Renders models into HTML, JPG, PNG, etc...
     */
    public static ModelHtmlRenderer of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return
        CompositeModelHtmlRenderer.of(ModelHtmlRendererMap.RENDERERS)
            .with(DatabaseModelHtmlRenderers.of(db,serversStore,hintsStore,managedFunction))
            .with(LogModelHtmlRenderers.RENDERERS)
            .with(AltModelHtmlRenderers.RENDERERS)
        ;
    }
}
