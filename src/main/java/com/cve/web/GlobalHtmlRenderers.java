package com.cve.web;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Defines the mapping we use for the entire application.
 * @author curt
 */
final class GlobalHtmlRenderers implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final ModelHtmlRenderer renderer;

    private GlobalHtmlRenderers() {
        
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(ExitPage.class,         PageDecorator.of(ExitPageRenderer.of()));
        map.put(StringModel.class,      PageDecorator.of(StringModelRenderer.of()));
        renderer = ClassMapModelHtmlRenderer.of(map);
    }

    public static GlobalHtmlRenderers of() {
        return new GlobalHtmlRenderers();
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }


}
