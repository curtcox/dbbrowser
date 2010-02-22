package com.cve.web.management;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.web.core.renderers.ClassMapModelHtmlRenderer;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.PageDecorator;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Renderers for management pages.
 */
public final class ManagementModelHtmlRenderers implements ModelHtmlRenderer {

    final Log log = Logs.of();

    final ModelHtmlRenderer renderer;

    private ManagementModelHtmlRenderers() {
        
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.put(AnnotatedStackTraceModel.class,     AnnotatedStackTraceRenderer.of());
        map.put(ObjectModel.class,                  ObjectModelRenderer.of());
        map.put(ObjectRegistryModel.class,          ObjectRegistryModelRenderer.of());
        map.put(PageRequestServiceModel.class,      PageDecorator.of(PageRequestModelRenderer.of()));
        map.put(PageRequestIndexModel.class,        PageDecorator.of(PageRequestIndexRenderer.of()));
        renderer = ClassMapModelHtmlRenderer.of(map);
    }

    public static ManagementModelHtmlRenderers of() {
        return new ManagementModelHtmlRenderers();
    }
    
    @Override
    public UIElement render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }

}
