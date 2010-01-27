package com.cve.web;

import com.cve.util.Check;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import com.cve.log.Log;
import static com.cve.util.Check.notNull;
/**
 * Finds the proper renderer from those it is given and delegates.
 * @author curt
 */
public final class ClassMapModelHtmlRenderer implements ModelHtmlRenderer {

    /**
     * Where we log to.
     */
    private final Log log;

    /**
     * Class -> Renderer
     */
    private final ImmutableMap<Class,ModelHtmlRenderer> renderers;

    private ClassMapModelHtmlRenderer(ImmutableMap<Class,ModelHtmlRenderer> renderers, Log log) {
        this.log = notNull(log);
        this.renderers = notNull(renderers);
    }

    public static ClassMapModelHtmlRenderer of(Map<Class,ModelHtmlRenderer> renderers, Log log) {
        ImmutableMap<Class,ModelHtmlRenderer> map = ImmutableMap.copyOf(renderers);
        return new ClassMapModelHtmlRenderer(map,log);
    }

    ClassMapModelHtmlRenderer with(Map<Class,ModelHtmlRenderer> addedRenderers) {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.putAll(renderers);
        map.putAll(addedRenderers);
        return of(map,log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        Check.notNull(model);
        Class toRender = model.getClass();
        ModelHtmlRenderer renderer = renderers.get(model.getClass());
        if (renderer==null) {
            String message = "No renderer for " + toRender.toString();
            throw new IllegalArgumentException(message);
        }
        return renderer.render(model,client);
    }

}
