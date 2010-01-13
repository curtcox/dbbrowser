
package com.cve.html;

import com.cve.util.Replace;

/**
 * Gateway class for any CSS stuff we do.
 * @author curt
 */
public enum CSS {

    NONE("",""),

    /**
     * For a cell with things you can do.
     */
    ACTIONS("actions","DDDDFF"),
    
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
        return " td {" + value + " }\r";
    }

    /**
     * Return the given string in brackets.
     */
    private static String b(String value) {
        return "{" + value + " }";
    }

    public static final String TABLE_COLORING =
        rowColoring(SERVER) +
        rowColoring(DATABASE) +
        rowColoring(TABLE) +
        rowColoring(ACTIONS) +
        rowColoring(EVEN_ROW) +
        rowColoring(ODD_ROW) +

        cellColoring(SERVER) +
        cellColoring(DATABASE) +
        cellColoring(TABLE) +
        cellColoring(COLUMN) +
        cellColoring(ROW_COUNT) +
        cellColoring(COLUMN_JOIN) +
        cellColoring(ACTIONS)
    ;

    static String rowColoring(CSS css) {
        return "tr." + css.value + td(background(css));
    }

    static String cellColoring(CSS css) {
        return "td." + css.value + b(background(css));
    }

    public static final String SHEET =
        Replace.bracketQuote("<style type=[text/css]>") +
        "img"          + b("border: medium none;") + // don't outline images
        TABLE_COLORING +
        "</style>"
    ;

}
