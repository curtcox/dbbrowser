package com.cve.web.log.browsers;

import com.cve.web.log.AbstractBrowser;
import java.util.*;


/**
 * @author ccox
 */
public final class CollectionBrowser
    extends AbstractBrowser
{

    public CollectionBrowser() {
        super(Collection.class);
    }

    /* (non-Javadoc)
     */
    @Override
    public String getComponentFor(Object o) {
        return "";
    }

}
