package com.cve.ui;

import com.cve.html.CSS;
import com.cve.lang.URIObject;
import com.cve.log.Log;
import com.cve.log.Logs;

import java.util.List;

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

    public UITable table(UITableRow... rows) {
        return UITable.of(rows);
    }

    public UITableDetail detail(String value) {
        return UITableDetail.of(value);
    }

    public UITableRow row(UITableDetail... details) {
        return UITableRow.of(details);
    }

    public UITableRow row(UIElement... elements) {
        return UITableRow.of(elements);
    }

     public UITableRow row(CSS css, UITableCell... elements) {
        return UITableRow.of(css, elements);
    }

    public UITableRow row(List<UITableCell> elements, CSS css) {
        return UITableRow.of(elements,css);
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

    public static UISubmit submit(String value, URIObject icon) {
        return UISubmit.valueIcon(value,icon);
    }

    public static UITableHeader header(String s)          { return UITableHeader.of(s);   }
    public static UITableDetail detail(String s, CSS css) { return UITableDetail.of(s,css); }
    public static UITableRow row(UITableCell... cells)    { return UITableRow.of(cells); }

}
