package com.cve.web;

import com.cve.db.dbio.DBMetaData;
import com.cve.stores.ServersStore;
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
    public static ModelHtmlRenderer of(DBMetaData.Factory db, ServersStore serversStore) {
        return
        CompositeModelHtmlRenderer.of(ModelHtmlRendererMap.RENDERERS)
            .with(DatabaseModelHtmlRenderers.of(db,serversStore))
            .with(LogModelHtmlRenderers.RENDERERS)
            .with(AltModelHtmlRenderers.RENDERERS)
        ;
    }
}
