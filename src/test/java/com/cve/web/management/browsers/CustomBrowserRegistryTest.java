package com.cve.web.management.browsers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class CustomBrowserRegistryTest {

    @Test
    public void hashmapHasCustom() {
        assertTrue(getFor(Maps.newHashMap()).length > 0);
    }

    @Test
    public void hashmultimapHasCustom() {
        assertTrue(getFor(HashMultimap.create()).length > 0);
    }

    @Test
    public void arraylistHasCustom() {
        assertTrue(getFor(Lists.newArrayList()).length > 0);
    }

    CustomBrowser[] getFor(Object o) {
        return CustomBrowserRegistry.of().getBrowsersFor(o);
    }
}
