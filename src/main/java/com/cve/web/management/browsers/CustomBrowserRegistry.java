package com.cve.web.management.browsers;

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
final class CustomBrowserRegistry {

    // class -> browser
    private final Map<Class,CustomBrowser> registry = Maps.newHashMap();
    
    private final CustomBrowser ARRAY_BROWSER;

    private CustomBrowserRegistry() {
        // Register all custom browsers.
        register(CollectionBrowser.of());
        register(MapBrowser.of());
        register(MultimapBrowser.of());
        register(AnnotatedStackTraceBrowser.of());
        // add new custom browsers here
        ARRAY_BROWSER = ArrayBrowser.of();
    }

    public static CustomBrowserRegistry of() {
        return new CustomBrowserRegistry();
    }
    
    /**
     * Return all browsers that are appropriate for this object.
     */
    CustomBrowser[] getBrowsersFor(Object o) {
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
    private void register(CustomBrowser browser) {
        registry.put(browser.isBrowserFor(),browser);
    }
}
