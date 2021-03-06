package com.cve.ui;

import com.cve.lang.URIObject;
import static com.cve.util.Check.notNull;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.PageRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
     * The URIObject to invoke when the form is submitted.
     */
    private final URIObject action;

    /**
     * GET or POST?
     */
    private final PageRequest.Method method;

    /**
     * UI elements in the form.
     */
    private final ImmutableList<UIElement> elements;

    private final Log log = Logs.of();

    private final HTMLTags tags;

    /**
     * Create a new form that POSTs against the given URI.
     */
    public static UIForm postAction(URIObject action) {
        ImmutableList<UIElement> elements = ImmutableList.of();
        return new UIForm(action,PageRequest.Method.POST,elements);
    }

    /**
     * Create a new form that GETs against the given URI.
     */
    public static UIForm getAction(URIObject action) {
        ImmutableList<UIElement> elements = ImmutableList.of();
        return new UIForm(action,PageRequest.Method.GET,elements);
    }

    /**
     * Use a factory instead.
     */
    private UIForm(URIObject action, PageRequest.Method method, List<UIElement> elements) {
        this.action   = notNull(action);
        this.method   = notNull(method);
        this.elements = ImmutableList.copyOf(notNull(elements));
        
        tags = HTMLTags.of();
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
        return tags.form(action.toString(),method,body);
    }
}
