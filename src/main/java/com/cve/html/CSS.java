
package com.cve.html;

import com.cve.util.Replace;

/**
 *
 * @author curt
 */
public enum CSS {


    NONE("",""),
    HIDE("hide","880000"),
    SERVER("server","AAAAAA"),
    DATABASE("database","FF6666"),
    TABLE("table","66FF66"),
    COLUMN("column","8888FF"),
    /**
     * Used to indicate that a column name cell is for a column with
     * potential joins
     */
    COLUMN_JOIN("join","FFFF00"),
    EVEN_ROW("even","FFFFFF"),

    ODD_ROW("odd","DDDDBB");

    private final String value;

    private final String color;

    private CSS(String value, String color) {
        this.value = value;
        this.color = color;
    }

    @Override
    public String toString() {
        return value;
    }

    private static String background(CSS css) {
        return "background-color: " + css.color + ";";
    }

    private static String td(String value) {
        return "td {" + value + " }";
    }

    private static String b(String value) {
        return "{" + value + " }";
    }

    public static final String SHEET =
Replace.bracketQuote(
"<style type=[text/css]>" +
"tr.server   " + td(background(SERVER)) +
"tr.database " + td(background(DATABASE)) +
"tr.table    " + td(background(TABLE)) +
"tr.hide     " + td(background(HIDE)) +

"tr.even     " + td(background(EVEN_ROW)) +
"tr.odd      " + td(background(ODD_ROW)) +

"td.server   " + b(background(SERVER)) +
"td.database " + b(background(DATABASE)) +
"td.table    " + b(background(TABLE)) +
"td.column   " + b(background(COLUMN)) +
"td.join     " + b(background(COLUMN_JOIN)) +
"td.hide     " + b(background(HIDE)) +
"</style>"
);
 
}
