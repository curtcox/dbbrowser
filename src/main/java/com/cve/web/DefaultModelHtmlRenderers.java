package com.cve.web;

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
    static final ModelHtmlRenderer RENDERERS =
        CompositeModelHtmlRenderer.of(ModelHtmlRendererMap.RENDERERS)
            .with(DatabaseModelHtmlRenderers.RENDERERS)
            .with(LogModelHtmlRenderers.RENDERERS)
            .with(AltModelHtmlRenderers.RENDERERS)
        ;

}
