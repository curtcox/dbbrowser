package com.cve.web.management;

import com.cve.lang.URIObject;
import com.cve.lang.AnnotatedClass;
import com.cve.util.URIs;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class LogCodecTest {

    @Test  public void int7()    {    object(     7 );         }
    @Test  public void hello()   {    object("Hello");         }
    @Test  public void long999() {    object(   999L);         }
    @Test  public void string()  {    object(AnnotatedClass.of(String.class));  }
    @Test  public void uri()     {    object(URIs.of("http://example.com/"));   }

    void object(Object a) {
        LogCodec codec = LogCodec.of();
        URIObject encoded = codec.encode(a);
        Object b = codec.decode(encoded);
        assertEquals(a,b);
    }
}
