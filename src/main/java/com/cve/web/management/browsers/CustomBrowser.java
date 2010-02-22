package com.cve.web.management.browsers;

import com.cve.ui.UIElement;

/**
 * A browser that is specifically designed to browse a certain type of object.
 * This interface is used as an extension mechanism to provide additional
 * functionality for {@link ObjectBrowser}.  It can be ignored, unless a new
 * custom browser type is required.
 * <p>
 * The easiest way to define a new custom browser is
 * <ol>
 * <li> add a new class in the org.debug.browsers package that
 * extends AbstractBrowser (see the existing classes in that package for
 * examples)
 * <li> register the browser by adding a line in {@link CustomBrowserRegistry} 
 * </ol>
 * @author ccox
 */
public interface CustomBrowser {

    /**
     * Return the type of object that this browser is designed for. 
     */
    public Class isBrowserFor();

    /**
     * Given an object, return a component for browsing that object.
     */
    public UIElement getComponentFor(Object o);

} // Custom Browser
