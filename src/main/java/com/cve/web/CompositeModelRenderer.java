package com.cve.web;

import com.cve.util.Check;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Finds the proper renderer from those it is given and delegates.
 * @author curt
 */
final class CompositeModelRenderer implements ModelRenderer {

    /**
     * Class -> Renderer
     */
    private final ImmutableMap<Class,ModelRenderer> renderers;

    private CompositeModelRenderer(ImmutableMap<Class,ModelRenderer> renderers) {
        this.renderers = Check.notNull(renderers);
    }

    public static CompositeModelRenderer of(Map<Class,ModelRenderer> renderers) {
        ImmutableMap<Class,ModelRenderer> map = ImmutableMap.copyOf(renderers);
        return new CompositeModelRenderer(map);
    }

    CompositeModelRenderer with(Map<Class,ModelRenderer> addedRenderers) {
        Map<Class,ModelRenderer> map = Maps.newHashMap();
        map.putAll(renderers);
        map.putAll(addedRenderers);
        return of(map);
    }

    public Object render(Model model, ClientInfo client) {
        Check.notNull(model);
        Class toRender = model.getClass();
        ModelRenderer renderer = renderers.get(model.getClass());
        if (renderer==null) {
            String message = "No renderer for " + toRender.toString();
            throw new IllegalArgumentException(message);
        }
        return renderer.render(model,client);
    }

}
