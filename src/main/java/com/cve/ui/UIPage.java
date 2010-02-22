package com.cve.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * A composite UI element.
 * @author Curt
 */
public final class UIPage implements UIElement {

    private final String html;

    private final ImmutableList<UIElement> items;

    private UIPage(List<UIElement> items) {
        this.items = ImmutableList.copyOf(items);
        html = toHTML(items);
    }

    public static UIPage of(UIElement... items) {
        return new UIPage(ImmutableList.of(items));
    }

    public static UIPage of(List<UIElement> items) {
        return new UIPage(items);
    }

    public UIPage with(List<UIElement> moreItems) {
        List<UIElement> allItems = Lists.newArrayList();
        allItems.addAll(this.items);
        allItems.addAll(moreItems);
        return new UIPage(allItems);
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
