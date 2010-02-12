package com.cve.web.management;

import com.cve.util.Check;

/**
 * A skeletal implementation of {@link CustomBrowser}.
 * @author ccox
 */
public abstract class AbstractBrowser
    implements CustomBrowser
{

    /**
     * The kind of thing we provide special support for.
     */
    private final Class type;

    /**
     * Create a custom browser for the following type of thing.
     * @param type
     */
    public AbstractBrowser(Class type) {
        Check.notNull(type);
        this.type = type;
    }

    /**
     * Return the type of object that this browser is designed for. 
     */
    @Override
    public Class isBrowserFor() {
        return type;
    }

} // Custom Browser
