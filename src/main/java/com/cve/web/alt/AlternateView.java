package com.cve.web.alt;

import com.cve.lang.URIObject;
import com.cve.web.core.Icons;


/**
 * An alternate view of a SQL result set.
 * The application puts a lot of effort into producing a series of HTML
 * views.  Any of the result sets behind a HTML view can also be returned
 * as an alternate view.
 * @author Curt
 */
public enum AlternateView {

    SQL(Icons.SQL),
    CSV(Icons.CSV),
    XLS(Icons.XLS),
    PDF(Icons.PDF),
    XML(Icons.XML),
    JSON(Icons.JSON),
    COMPRESSED(Icons.COMPRESSED)
    ;

    public final URIObject icon;

    AlternateView(URIObject icon) {
        this.icon = icon;
    }
}
