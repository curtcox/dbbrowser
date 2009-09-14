package com.cve.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * A cascading menu user interface element.
 * @author Curt
 */
public final class UICascadingMenu implements UIElement {

    private final String html;

    private final ImmutableList<UIElement> items;

    private UICascadingMenu(List<UIElement> items) {
        this.items = ImmutableList.copyOf(items);
        html = toHTML(items);
    }

    public static UICascadingMenu of(List<UIElement> items) {
        return new UICascadingMenu(items);
    }

    public UICascadingMenu with(List<UIElement> moreItems) {
        List<UIElement> allItems = Lists.newArrayList();
        allItems.addAll(this.items);
        allItems.addAll(moreItems);
        return new UICascadingMenu(allItems);
    }

    private static String toHTML(List<UIElement> items) {
        StringBuilder out = new StringBuilder();
        for (UIElement element : items) {
            out.append(tr(td(element.toString())));
        }
        return table(out.toString());
    }

    @Override
    public String toString() {
        return html;
    }

    // Use our own copies to make sure we don't get carriage returns.
    static String    td(String s) { return "<td>" + s + "</td>"; }
    static String    tr(String s) { return "<tr>" + s + "</tr>"; }
    static String table(String s) { return "<table border>" + s + "</table>"; }

}
