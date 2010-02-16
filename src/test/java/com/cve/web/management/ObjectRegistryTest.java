package com.cve.web.management;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ObjectRegistryTest {

    @Test
    public void getGetsPut() {
        Object o = new Object();
        UniqueObjectKey key = ObjectRegistry.put(o);
        assertEquals(o,ObjectRegistry.get(key));
    }

    @Test
    public void keyParsesToString() {
        Object o = new Object();
        UniqueObjectKey key1 = ObjectRegistry.put(o);
        UniqueObjectKey key2 = UniqueObjectKey.parse(key1.toHexString());
        assertEquals(key1,key2);
    }

}
