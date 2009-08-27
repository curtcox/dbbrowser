package com.cve.web.alt;

/**
 * An alternate view of a SQL result set.
 * The application puts a lot of effort into producing a series of HTML
 * views.  Any of the result sets behind a HTML view can also be returned
 * as an alternate view.
 * @author Curt
 */
public enum AlternateView {

    SQL,
    CSV,
    XLS,
    PDF,
    XML,
    JSON;
}
