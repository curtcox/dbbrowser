
package com.cve.web.db.databases;

import com.cve.web.db.databases.DatabasesHandler;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class DatabasesHandlerTest {

    DatabasesHandler handler;

    @Test
    public void isServerOnlyRequest() {
        assertTrue(handler.isDatabaseListRequest("//server/"));
        assertTrue(handler.isDatabaseListRequest("//server"));
        assertTrue(handler.isDatabaseListRequest("//server:8080/"));
        assertTrue(handler.isDatabaseListRequest("//server:8080/"));
        assertFalse(handler.isDatabaseListRequest("//server/db/"));
        assertFalse(handler.isDatabaseListRequest("//server/db/table/"));
    }

    private static <T> ImmutableList<T> list(T... items) { return ImmutableList.of(items); }

}
