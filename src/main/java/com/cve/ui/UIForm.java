package com.cve.ui;

import static com.cve.util.Check.notNull;
import com.cve.html.HTML;
import com.cve.web.PageRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * Like a HTML form.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 * @author curt
 */
@Immutable
public final class UIForm {

    /**
     * The URI to invoke when the form is submitted.
     */
    private final URI action;

    /**
     * GET or POST?
     */
    private final PageRequest.Method method;

    /**
     * UI elements in the form.
     */
    private final ImmutableList<UIElement> elements;

    /**
     * Create a new form that POSTs against the given URI.
     */
    public static UIForm postAction(URI action) {
        ImmutableList<UIElement> elements = ImmutableList.of();
        return new UIForm(action,PageRequest.Method.POST,elements);
    }

    /**
     * Create a new form that GETs against the given URI.
     */
    public static UIForm getAction(URI action) {
        ImmutableList<UIElement> elements = ImmutableList.of();
        return new UIForm(action,PageRequest.Method.GET,elements);
    }

    /**
     * Use a factory instead.
     */
    private UIForm(URI action, PageRequest.Method method, List<UIElement> elements) {
        this.action   = notNull(action);
        this.method   = notNull(method);
        this.elements = ImmutableList.copyOf(notNull(elements));
    }

    /**
     * Return a form like this one, but with the given element added.
     */
    public UIForm with(UIElement element) {
        List<UIElement> newElements = Lists.newArrayList();
        newElements.addAll(elements);
        newElements.add(element);
        return new UIForm(action,method,newElements);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UIElement element : elements) {
            out.append(element);
        }
        String body = out.toString();
        return HTML.form(action.toString(),method,body);
    }
}
