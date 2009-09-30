package com.cve.stores;

import com.cve.stores.IO;
import com.google.common.base.Function;
import java.io.File;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class ActiveFunctionTest {

    static Function MAPPER_1 = new Function() {
        @Override
        public Object apply(Object k) {
            return k + "1";
        }
    };

    static IO IO_1 = new IO() {

    };

    @Test
    public void emptyMapContainsMappedValue() {
        File file = new File("test");
        Function map = ActiveFunction.fileMapper(file,IO_1,MAPPER_1);
        assertEquals("1",map.apply(""));
    }

}
