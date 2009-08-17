package com.cve.ui;

/**
 * Convenient static methods for building a UI.
 * @author curt
 */
public final class UIBuilder {

    public static UITable table(UIRow... rows) {
        return UITable.of(rows);
    }

    public static UIDetail detail(String value) {
        return UIDetail.of(value);
    }

    public static UIRow row(UIDetail... details) {
        return UIRow.of(details);
    }

    public static UIRow row(UIElement... elements) {
        return UIRow.of(elements);
    }

    public static UIText text(String name, String value) {
        return UIText.nameValue(name,value);
    }

    public static UILabel label(String value) {
        return UILabel.value(value);
    }

    public static UISubmit submit(String value) {
        return UISubmit.value(value);
    }
}
