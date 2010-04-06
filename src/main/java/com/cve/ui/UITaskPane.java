package com.cve.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * A generic accordian control.
 * The Swing implementation is JXTaskPane.
 * @author Curt
 */
public final class UITaskPane implements UIElement {

    private final String html;

    public final ImmutableList<UIElement> heading;
    public final ImmutableList<UIElement> items;

    private UITaskPane(List<UIElement> all) {
        if ((all.size() % 2) != 0) {
            throw new IllegalArgumentException();
        }
        List<UIElement> heading = Lists.newArrayList();
        List<UIElement> items   = Lists.newArrayList();
        for (int i=0; i<all.size(); i++) {
            UIElement e = all.get(i);
            if (i % 2 == 0) {
                heading.add(e);
            } else {
                items.add(e);
            }
        }
        this.heading = ImmutableList.copyOf(heading);
        this.items = ImmutableList.copyOf(items);
        html = toHTML(items);
    }

    public static UITaskPane of(UIElement... items) {
        return new UITaskPane(ImmutableList.of(items));
    }

    public static UITaskPane of(List<UIElement> items) {
        return new UITaskPane(items);
    }

    public UITaskPane with(List<UIElement> moreItems) {
        List<UIElement> allItems = Lists.newArrayList();
        allItems.addAll(this.items);
        allItems.addAll(moreItems);
        return new UITaskPane(allItems);
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
