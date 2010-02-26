package com.cve.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * A composite UI element.
 * @author Curt
 */
public final class UIComposite implements UIElement {

    private final String html;

    public final ImmutableList<UIElement> items;

    private UIComposite(List<UIElement> items) {
        this.items = ImmutableList.copyOf(items);
        html = toHTML(items);
    }

    public static UIComposite of(UIElement... items) {
        return new UIComposite(ImmutableList.of(items));
    }

    public static UIComposite of(List<UIElement> items) {
        return new UIComposite(items);
    }

    public UIComposite with(List<UIElement> moreItems) {
        List<UIElement> allItems = Lists.newArrayList();
        allItems.addAll(this.items);
        allItems.addAll(moreItems);
        return new UIComposite(allItems);
    }

    private static String toHTML(List<UIElement> items) {
        StringBuilder out = new StringBuilder();
        for (UIElement element : items) {
            out.append(element.toString());
        }
        return out.toString();
    }

    @Override
    public String toString() {
        return html;
    }

}
