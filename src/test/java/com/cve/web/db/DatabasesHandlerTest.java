
package com.cve.web.db;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class DatabasesHandlerTest {

    @Test
    public void isServerOnlyRequest() {
        assertTrue(DatabasesHandler.isDatabaseListRequest("/server/"));
        assertTrue(DatabasesHandler.isDatabaseListRequest("/server"));
        assertTrue(DatabasesHandler.isDatabaseListRequest("/server:8080/"));
        assertTrue(DatabasesHandler.isDatabaseListRequest("/server:8080/"));
        assertFalse(DatabasesHandler.isDatabaseListRequest("/server/db/"));
        assertFalse(DatabasesHandler.isDatabaseListRequest("/server/db/table/"));
    }

    private static <T> ImmutableList<T> list(T... items) { return ImmutableList.of(items); }

}
