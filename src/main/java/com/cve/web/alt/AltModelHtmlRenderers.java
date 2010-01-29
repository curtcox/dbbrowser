package com.cve.web.alt;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.ClassMapModelHtmlRenderer;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.google.common.collect.Maps;
import java.util.Map;
/**
 * Renderers for alternate views of result sets.
 * @author Curt
 */
public final class AltModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final ModelHtmlRenderer renderer;

    private AltModelHtmlRenderers() {
        
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        renderer = ClassMapModelHtmlRenderer.of(map);
    }
    
    public static AltModelHtmlRenderers of() {
        return new AltModelHtmlRenderers();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }
}
