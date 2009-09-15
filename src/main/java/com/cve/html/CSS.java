
package com.cve.html;

import com.cve.util.Replace;

/**
 * Gateway class for any CSS stuff we do.
 * @author curt
 */
public enum CSS {

    NONE("",""),
    
    ACTIONS("actions","880000"),
    
    /**
     * For server names
     */
    SERVER("server","BBBBBB"),

    /**
     * For database names
     */
    DATABASE("database","FF8888"),

    /**
     * For table names
     */
    TABLE("table","88FF88"),

    /**
     * For column names
     */
    COLUMN("column","BBBBFF"),

    /**
     * For row counts
     */
    ROW_COUNT("rowCount","CCCCCC"),

    /**
     * Used to indicate that a column name cell is for a column with
     * potential joins
     */
    COLUMN_JOIN("join","FFFF00"),

    /**
     * For even rows in tables
     */
    EVEN_ROW("even","FFFFFF"),

    /**
     * For odd rows in tables
     */
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

    /**
     * Return the given string in brackets.
     */
    private static String b(String value) {
        return "{" + value + " }";
    }

    public static String TABLE_COLORING =
        "tr.server   " + td(background(SERVER)) +
        "tr.database " + td(background(DATABASE)) +
        "tr.table    " + td(background(TABLE)) +
        "tr.hide     " + td(background(ACTIONS)) +

        "tr.even     " + td(background(EVEN_ROW)) +
        "tr.odd      " + td(background(ODD_ROW)) +

        "td.server   " + b(background(SERVER)) +
        "td.database " + b(background(DATABASE)) +
        "td.table    " + b(background(TABLE)) +
        "td.column   " + b(background(COLUMN)) +
        "td.rowCount " + b(background(ROW_COUNT)) +
        "td.join     " + b(background(COLUMN_JOIN)) +
        "td.actions  " + b(background(ACTIONS))
    ;
    
    public static final String SHEET =
        Replace.bracketQuote("<style type=[text/css]>") +
        "img"          + b("border: medium none;") + // don't outline images
        TABLE_COLORING +
        "</style>"
    ;

}
