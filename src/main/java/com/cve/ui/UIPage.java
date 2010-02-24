package com.cve.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * A composite UI element.
 * @author Curt
 */
@Immutable
public final class UIPage implements UIElement {

    public final ImmutableList<UIElement> items;

    private UIPage(List<UIElement> items) {
        this.items = ImmutableList.copyOf(items);
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

    @Override
    public String toString() {
        return items.toString();
    }

}
