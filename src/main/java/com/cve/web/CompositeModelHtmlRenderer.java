package com.cve.web;

import com.cve.util.Check;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import static com.cve.log.Log.args;
/**
 * Finds the proper renderer from those it is given and delegates.
 * @author curt
 */
final class CompositeModelHtmlRenderer implements ModelHtmlRenderer {

    /**
     * Class -> Renderer
     */
    private final ImmutableMap<Class,ModelHtmlRenderer> renderers;

    private CompositeModelHtmlRenderer(ImmutableMap<Class,ModelHtmlRenderer> renderers) {
        this.renderers = Check.notNull(renderers);
    }

    public static CompositeModelHtmlRenderer of(Map<Class,ModelHtmlRenderer> renderers) {
        ImmutableMap<Class,ModelHtmlRenderer> map = ImmutableMap.copyOf(renderers);
        return new CompositeModelHtmlRenderer(map);
    }

    CompositeModelHtmlRenderer with(Map<Class,ModelHtmlRenderer> addedRenderers) {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.putAll(renderers);
        map.putAll(addedRenderers);
        return of(map);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        args(model,client);
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
