package com.cve.ui;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * An ordered set of UI elements.
 * A better name might be UIList, but that has formatting connotations.
 * A series is for when you want a table cell to contain a list of things, but
 * don't want to add any line breaks.
 * @author curt
 */
@Immutable
public final class UISeries implements UIElement {

    final ImmutableList<UIElement> elements;

    private UISeries(Collection<UIElement> elements) {
        this.elements = ImmutableList.copyOf(elements);
    }

    public static UISeries of(Collection<UIElement> elements) {
        return new UISeries(elements);
    }

    public static UISeries of(List<UIElement> elements) {
        return new UISeries(elements);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UIElement element : elements) {
            out.append(element.toString());
        }
        return out.toString();
    }
}
