package com.cve.web;

import com.cve.log.Log;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import static com.cve.util.Check.notNull;
/**
 * Converts a model to HTML in stages.
 * @author curt
 */
final class StagedModelHtmlRenderer implements ModelHtmlRenderer {

    final Log log;

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

    private StagedModelHtmlRenderer(ImmutableList<Stage> renderers, Log log) {
        Map<Class,Stage> map = Maps.newHashMap();
        for (Stage stage : renderers) {
            map.put(stage.target, stage);
        }
        stages = ImmutableMap.copyOf(map);
        this.log = notNull(log);
    }

    public static StagedModelHtmlRenderer of(List<Stage> renderers, Log log) {
        ImmutableList<Stage> list = ImmutableList.copyOf(renderers);
        return new StagedModelHtmlRenderer(list,log);
    }

    StagedModelHtmlRenderer with(List<Stage> addedRenderers) {
        List<Stage> list = Lists.newArrayList();
        list.addAll(stages.values());
        list.addAll(addedRenderers);
        return of(list,log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
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
            if (rendered instanceof String) {
                return HtmlPage.guts((String) rendered,log);
            }
            toRender = rendered;
        }
        throw new IllegalArgumentException();
    }

}
