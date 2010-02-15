package com.cve.web.management;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.ClassMapModelHtmlRenderer;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import com.cve.web.PageDecorator;
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
        map.put(ManagementFactoryModel.class,       ManagementFactoryModelRenderer.of());
        map.put(PageRequestServiceModel.class,      PageDecorator.of(PageRequestModelRenderer.of()));
        map.put(PageRequestIndexModel.class,        PageDecorator.of(PageRequestIndexRenderer.of()));
        renderer = ClassMapModelHtmlRenderer.of(map);
    }

    public static ManagementModelHtmlRenderers of() {
        return new ManagementModelHtmlRenderers();
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        return renderer.render(model, client);
    }

}
