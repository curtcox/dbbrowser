package com.cve.ui;

/**
 * For constructing user interfaces.
 * This interface provides a standard mechanism for constructing user interfaces.
 * @author curt
 */
public interface UIConstructor {

    /**
     * Given a description of the page user interface, construct an actual
     * page user interface.  The type of user interface returned is
     * implementation-specific.  An HTML constructor would return a String or
     * an HTML object.  A Swing constructor will return a JComponent.
     */
    Object construct(UIPage page);
}
