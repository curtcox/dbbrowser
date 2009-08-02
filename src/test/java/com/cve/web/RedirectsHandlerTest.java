package com.cve.web;

import com.cve.util.URIs;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class RedirectsHandlerTest {

    @Test
    public void handled() throws IOException {
        RedirectsHandler     handler = new RedirectsHandler();
        PageRequest   request = PageRequest.path("/");
        PageResponse response = handler.produce(request);
        assertNull(response);
    }

    @Test
    public void joins() throws SQLException {
        assertRedirected("/server/db/db.t/db.t.c1/join","db.t.c1=db.t2.c2","/server/db/db.t+db.t2/db.t.c1/db.t.c1=db.t2.c2/");
    }

    @Test
    public void filters() throws SQLException {
        assertRedirected("/server/db/db.t/db.t.c1//filter","db.t.c1=7",    "/server/db/db.t/db.t.c1//db.t.c1=7/");
    }

    @Test
    public void hides() throws SQLException {
        assertRedirected("/server/db/db.t/db.t.c1+db.t.c2/hide","db.t.c1", "/server/db/db.t/db.t.c2/");
    }

    private void assertRedirected(String path, String query, String dest) throws SQLException {
        ImmutableMap<String,String> parameters = ImmutableMap.of();
        assertEquals(URIs.of(dest),RedirectsHandler.redirectsActionsTo(path,query));
    }
}
