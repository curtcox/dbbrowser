package com.cve.ui;

/**
 *
 * @author curt
 */
public interface UIConstructor {
    Object construct(UIElement e);
    Object label(UILabel label);
    Object page(UIPage page);
    Object composite(UIComposite composite);
    Object table(UITable table);
}
