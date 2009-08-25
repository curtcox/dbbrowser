package com.cve.web.log;

import com.cve.web.log.ObjectRegistry.Key;
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
        Key key = ObjectRegistry.put(o);
        assertEquals(o,ObjectRegistry.get(key));
    }

    @Test
    public void keyParsesToString() {
        Object o = new Object();
        Key key1 = ObjectRegistry.put(o);
        Key key2 = Key.parse(key1.toHexString());
        assertEquals(key1,key2);
    }

}
