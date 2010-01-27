package com.cve.web;

import com.cve.util.Check;
import com.cve.log.Log;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import static com.cve.util.Check.notNull;
/**
 * Finds the proper renderer from those it is given and delegates.
 * @author curt
 */
public final class CompositeModelHtmlRenderer implements ModelHtmlRenderer {

    /**
     * Where we log to.
     */
    private final Log log;

    /**
     * Class -> Renderer
     */
    private final ImmutableList<ModelHtmlRenderer> renderers;

    private CompositeModelHtmlRenderer(ImmutableList<ModelHtmlRenderer> renderers, Log log) {
        this.log = notNull(log);
        this.renderers = notNull(renderers);
    }

    public static CompositeModelHtmlRenderer of(Log log, ModelHtmlRenderer... renderers) {
        ImmutableList<ModelHtmlRenderer> list = ImmutableList.of(renderers);
        return new CompositeModelHtmlRenderer(list,log);
    }

    public static CompositeModelHtmlRenderer of(List<ModelHtmlRenderer> renderers, Log log) {
        ImmutableList<ModelHtmlRenderer> list = ImmutableList.copyOf(renderers);
        return new CompositeModelHtmlRenderer(list,log);
    }

    CompositeModelHtmlRenderer with(ModelHtmlRenderer addedRenderer) {
        List<ModelHtmlRenderer> list = Lists.newArrayList();
        list.addAll(renderers);
        list.add(addedRenderer);
        return of(list,log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        Check.notNull(model);
        for (ModelHtmlRenderer renderer : renderers) {
            HtmlPage page = renderer.render(model, client);
            if (page!=null) {
                return page;
            }
        }
        return null;
    }

}
