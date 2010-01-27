package com.cve.web;

import com.cve.log.Log;
import com.google.common.collect.Maps;
import java.util.Map;
import static com.cve.util.Check.notNull;

/**
 * Defines the mapping we use for the entire application.
 * @author curt
 */
final class GlobalHtmlRenderers implements ModelHtmlRenderer {

    final Log log;

    final ModelHtmlRenderer renderer;

    private GlobalHtmlRenderers(Log log) {
        this.log = notNull(log);
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(ExitPage.class,         PageDecorator.of(ExitPageRenderer.of(log)));
        map.put(StringModel.class,      PageDecorator.of(StringModelRenderer.of(log)));
        renderer = ClassMapModelHtmlRenderer.of(map, log);
    }

    public static GlobalHtmlRenderers of(Log log) {
        return new GlobalHtmlRenderers(log);
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }


}
