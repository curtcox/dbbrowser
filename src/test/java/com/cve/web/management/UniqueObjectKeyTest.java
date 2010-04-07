package com.cve.web.management;

import com.cve.web.core.PageRequest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class UniqueObjectKeyTest {

    @Test  public void int7()            {    object(     7 );    }
    @Test  public void hello()           {    object("Hello");    }
    @Test  public void long999()         {    object(   999L);    }
    @Test  public void pageRequestNull() {    object(PageRequest.NULL);    }

    void object(Object o) {
        UniqueObjectKey key1 = UniqueObjectKey.of(o);
        assertEquals(o.hashCode(),key1.hash);
        assertEquals(o.getClass().hashCode(),key1.clazz);
        UniqueObjectKey key2 = UniqueObjectKey.parse(key1.toString());
        assertEquals(key1,key2);
        assertEquals(key1.hash,key2.hash);
        assertEquals(key1.hash64,key2.hash64);
        assertEquals(key1.clazz,key2.clazz);
    }

    @Test public void parse() {
        UniqueObjectKey.parse("d95a62ad00b914b3");
    }
}
