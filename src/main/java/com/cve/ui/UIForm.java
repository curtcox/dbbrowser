package com.cve.ui;

import static com.cve.util.Check.notNull;
import com.cve.html.HTML;
import com.cve.web.PageRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;

/**
 * Like a HTML form.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 * @author curt
 */
public final class UIForm {

    private final URI action;
    private final PageRequest.Method method;
    private final ImmutableList<UIElement> elements;

    public static UIForm postAction(URI action) {
        ImmutableList<UIElement> elements = ImmutableList.of();
        return new UIForm(action,PageRequest.Method.POST,elements);
    }

    private UIForm(URI action, PageRequest.Method method, List<UIElement> elements) {
        this.action   = notNull(action);
        this.method   = notNull(method);
        this.elements = ImmutableList.copyOf(notNull(elements));
    }

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
