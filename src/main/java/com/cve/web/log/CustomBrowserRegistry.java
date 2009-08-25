package com.cve.web.log;

import com.cve.util.Check;
import com.cve.web.log.browsers.ArrayBrowser;
import com.cve.web.log.browsers.CollectionBrowser;
import com.cve.web.log.browsers.MapBrowser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;


/**
 * A registry of {@link CustomBrowser}S.
 * Currently, the registry only allows one browser to be registered for any
 * class.  Even so, several browsers may be appropriate for a given object.
 * For instance, if browsers were registered for {@link java.util.Collection}
 * and {@java.util.ArrayList} then <code>getBrowsersFor(new ArrayList())</code>
 * would return both browsers.
 * @author ccox
 */
class CustomBrowserRegistry {

    // class -> browser
    private static final Map<Class,CustomBrowser> registry = Maps.newHashMap();
    
    private static final CustomBrowser ARRAY_BROWSER = new ArrayBrowser();

    /**
     * Register all custom browsers.
     */
    static {
        register(new CollectionBrowser());
        register(new MapBrowser());
        // add new custom browsers here
    }

    private CustomBrowserRegistry() {
        // don't instantiate
    }

    /**
     * Return all browsers that are appropriate for this object.
     */
    static CustomBrowser[] getBrowsersFor(Object o) {
        if (o==null) {
            return new CustomBrowser[0];
        }
        Class objectClass = o.getClass();
        List<CustomBrowser> choices = Lists.newArrayList();
        if (objectClass.isArray()) {
            choices.add(ARRAY_BROWSER);
        }
        for (Class browsedClass : registry.keySet()) {
            if (browsedClass.isAssignableFrom(objectClass)) {
                choices.add(registry.get(browsedClass));
            }
        }
        return (CustomBrowser[]) choices.toArray(new CustomBrowser[0]);
    }

    /**
     * Register this browser.
     * This is not a public method, since this class must know about all
     * CustomBrowsers to ensure that they are registered.  
     */
    private static void register(CustomBrowser browser) {
        registry.put(browser.isBrowserFor(),browser);
    }
}
