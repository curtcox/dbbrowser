package com.cve.web.core.renderers;

import com.cve.web.core.ModelHtmlRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.util.Check;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
/**
 * Converts a model to HTML in stages.
 * @author curt
 */
final class StagedModelHtmlRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    public abstract class Stage {

        final Class target;

        Stage(Class target) {
            this.target = target;
        }

        /**
         * Given a model, render it the way we do.
         */
        abstract Object render(Model model, ClientInfo client);
    }

    /**
     * Class -> Renderer
     */
    private final ImmutableMap<Class,Stage> stages;

    private StagedModelHtmlRenderer(ImmutableList<Stage> renderers) {
        Map<Class,Stage> map = Maps.newHashMap();
        for (Stage stage : renderers) {
            map.put(stage.target, stage);
        }
        stages = ImmutableMap.copyOf(map);
        
    }

    public static StagedModelHtmlRenderer of(List<Stage> renderers) {
        ImmutableList<Stage> list = ImmutableList.copyOf(renderers);
        return new StagedModelHtmlRenderer(list);
    }

    StagedModelHtmlRenderer with(List<Stage> addedRenderers) {
        List<Stage> list = Lists.newArrayList();
        list.addAll(stages.values());
        list.addAll(addedRenderers);
        return of(list);
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        Check.notNull(model);
        Object toRender = model;
        Object rendered = null;
        for (int i=0; i<100; i++) {
            Stage renderer = stages.get(toRender.getClass());
            if (renderer==null) {
                String message = "No renderer for " + toRender.toString();
                throw new IllegalArgumentException(message);
            }
            rendered = renderer.render(model,client);
            if (rendered instanceof UIElement) {
                return ((UIElement) rendered);
            }
            toRender = rendered;
        }
        throw new IllegalArgumentException();
    }

}
