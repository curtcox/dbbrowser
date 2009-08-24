package com.cve.web.log;

import com.cve.util.Check;

/**
 * A skeletal implementation of {@link CustomBrowser}.
 * @author ccox
 */
public abstract class AbstractBrowser
    implements CustomBrowser
{

    private final Class type;

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
