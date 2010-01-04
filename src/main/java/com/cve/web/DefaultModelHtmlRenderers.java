package com.cve.web;

import com.cve.db.dbio.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.HintsStore;
import com.cve.stores.db.ServersStore;
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
        DBMetaData.Factory db, ServersStore serversStore, HintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return
        CompositeModelHtmlRenderer.of(ModelHtmlRendererMap.RENDERERS)
            .with(DatabaseModelHtmlRenderers.of(db,serversStore,hintsStore,managedFunction))
            .with(LogModelHtmlRenderers.RENDERERS)
            .with(AltModelHtmlRenderers.RENDERERS)
        ;
    }
}
