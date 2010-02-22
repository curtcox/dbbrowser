package com.cve.web.core.renderers;

import com.cve.web.core.ModelHtmlRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.models.ExitPage;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.PageDecorator;
import com.cve.web.core.models.StringModel;
import com.cve.web.core.models.TextFileModel;
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
        map.put(TextFileModel.class,    PageDecorator.of(TextFileModelHtmlRenderer.of()));
        renderer = ClassMapModelHtmlRenderer.of(map);
    }

    public static GlobalHtmlRenderers of() {
        return new GlobalHtmlRenderers();
    }
    
    @Override
    public UIElement render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }


}
