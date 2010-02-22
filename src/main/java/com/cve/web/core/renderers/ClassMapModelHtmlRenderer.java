package com.cve.web.core.renderers;

import com.cve.web.core.ModelHtmlRenderer;
import com.cve.util.Check;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.Model;
import static com.cve.util.Check.notNull;
/**
 * Finds the proper renderer from those it is given and delegates.
 * The name "ClassMap" comes from the fact that this renderer uses the class
 * of the model to determine which renderer to delegate to.
 * @author curt
 */
public final class ClassMapModelHtmlRenderer implements ModelHtmlRenderer {

    /**
     * Where we log to.
     */
    private final Log log = Logs.of();

    /**
     * Class -> Renderer
     */
    private final ImmutableMap<Class,ModelHtmlRenderer> renderers;

    private ClassMapModelHtmlRenderer(ImmutableMap<Class,ModelHtmlRenderer> renderers) {
        
        this.renderers = notNull(renderers);
    }

    public static ClassMapModelHtmlRenderer of(Map<Class,ModelHtmlRenderer> renderers) {
        ImmutableMap<Class,ModelHtmlRenderer> map = ImmutableMap.copyOf(renderers);
        return new ClassMapModelHtmlRenderer(map);
    }

    ClassMapModelHtmlRenderer with(Map<Class,ModelHtmlRenderer> addedRenderers) {
        Map<Class,ModelHtmlRenderer> map = Maps.newHashMap();
        map.putAll(renderers);
        map.putAll(addedRenderers);
        return of(map);
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        log.args(model,client);
        Check.notNull(model);
        Class toRender = model.getClass();
        ModelHtmlRenderer renderer = renderers.get(toRender);
        if (renderer==null) {
            return null;
        }
        return renderer.render(model,client);
    }

}
