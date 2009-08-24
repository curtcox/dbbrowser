package com.cve.web.log.browsers;

import com.cve.web.log.AbstractBrowser;

/**
 * @author ccox
 */
public final class ArrayBrowser
    extends AbstractBrowser
{

    public ArrayBrowser() {
        super(Object.class); // harmless lie
    }

    /* (non-Javadoc)
     * @see com.tripos.hts.gui.debug.CustomBrowser#getComponentFor(java.lang.Object)
     */
    @Override
    public String getComponentFor(Object o) {
        return "";
    }

}
