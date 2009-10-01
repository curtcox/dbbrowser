package com.cve.stores;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class ActiveFunctionTest {

    static SQLFunction MAPPER_1 = new SQLFunction() {
        @Override
        public Object apply(Object k) {
            return k + "1";
        }
    };

    static IO IO_1 = new IO() {

    };

    @Test
    public void emptyMapContainsMappedValue() throws IOException, SQLException {
        File file = new File("test");
        SQLFunction map = ActiveFunction.fileIOFunc(file,IO_1,MAPPER_1);
        assertEquals("1",map.apply(""));
    }

}
