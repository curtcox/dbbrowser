package com.cve.ui;

import com.cve.util.Check;
import com.cve.util.Replace;
import javax.annotation.concurrent.Immutable;

/**
 * Like a HTML form text field.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
@Immutable
public final class UITextArea implements UIElement {

    final String name;

    final String value;

    final int rows;

    final int columns;

    private UITextArea(String name, String value, int rows, int columns) {
        this.name  = Check.notNull(name);
        this.value = Check.notNull(value);
        this.rows = rows;
        this.columns = columns;
    }
    
    public static UITextArea nameValueRowsColumns(String name, String value, int rows, int columns) {
        return new UITextArea(name,value,rows,columns);
    }

    @Override
    public String toString() {
        return Replace.bracketQuote(
            "<textarea name=["+ name + "] rows=[" + rows + "] cols=[" + columns + "]>" + value + "</textarea>"
        );
    }
}
