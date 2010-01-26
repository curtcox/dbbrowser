package com.cve.ui;

import com.cve.log.Log;
import java.net.URI;
import static com.cve.util.Check.notNull;

/**
 * Convenient static methods for building a UI.
 * @author curt
 */
public final class UIBuilder {

    final Log log;

    private UIBuilder(Log log) {
        this.log = notNull(log);
    }

    public static UIBuilder of(Log log) {
        return new UIBuilder(log);
    }

    public UITable table(UIRow... rows) {
        return UITable.of(log, rows);
    }

    public UIDetail detail(String value) {
        return UIDetail.of(value,log);
    }

    public UIRow row(UIDetail... details) {
        return UIRow.of(log,details);
    }

    public UIRow row(UIElement... elements) {
        return UIRow.of(log,elements);
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
        return UILabel.value(value);
    }

    public static UISubmit submit(String value) {
        return UISubmit.value(value);
    }

    public static UISubmit submit(String value, URI icon) {
        return UISubmit.valueIcon(value,icon);
    }

}
