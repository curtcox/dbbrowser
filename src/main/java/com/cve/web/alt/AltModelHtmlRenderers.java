package com.cve.web.alt;

import com.cve.log.Log;
import com.cve.web.ClassMapModelHtmlRenderer;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.google.common.collect.Maps;
import java.util.Map;
import static com.cve.util.Check.notNull;
/**
 * Renderers for alternate views of result sets.
 * @author Curt
 */
public final class AltModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log;

    final ModelHtmlRenderer renderer;

    private AltModelHtmlRenderers(Log log) {
        this.log = notNull(log);
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        renderer = ClassMapModelHtmlRenderer.of(map,log);
    }
    
    public static AltModelHtmlRenderers of(Log log) {
        return new AltModelHtmlRenderers(log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }
}
