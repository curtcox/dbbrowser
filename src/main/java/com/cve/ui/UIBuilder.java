package com.cve.ui;

import com.cve.log.Log;
import com.cve.log.Logs;
import java.net.URI;

/**
 * Convenient methods for building a UI.
 * @author curt
 */
public final class UIBuilder {

    final Log log = Logs.of();

    /**
     * Don't make me.
     */
    private UIBuilder() {}

    public static UIBuilder of() {
        return new UIBuilder();
    }

    public UITable table(UIRow... rows) {
        return UITable.of(rows);
    }

    public UIDetail detail(String value) {
        return UIDetail.of(value);
    }

    public UIRow row(UIDetail... details) {
        return UIRow.of(details);
    }

    public UIRow row(UIElement... elements) {
        return UIRow.of(elements);
    }

    /**
     *
     */
    public static UIText text(String name, String value) {
        return UIText.nameValue(name,value);
    }

    public static UITextArea textArea(String name, String value, int rows, int columns) {
        return UITextArea.nameValueRowsColumns(name,value,rows,columns);
    }

    public static UILabel label(String value) {
        return UILabel.of(value);
    }

    public static UISubmit submit(String value) {
        return UISubmit.value(value);
    }

    public static UISubmit submit(String value, URI icon) {
        return UISubmit.valueIcon(value,icon);
    }

}
