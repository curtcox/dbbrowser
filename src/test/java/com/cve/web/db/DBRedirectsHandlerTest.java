package com.cve.web.db;

import com.cve.web.*;
import com.cve.util.URIs;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class DBRedirectsHandlerTest {

    @Test
    public void handled() throws IOException {
        DBRedirectsHandler   handler = new DBRedirectsHandler();
        PageRequest   request = PageRequest.path("/");
        PageResponse response = handler.produce(request);
        assertNull(response);
    }

    @Test
    public void joins() throws SQLException {
        assertRedirected("/+/server/db/db.t/db.t.c1/join","db.t.c1=db.t2.c2","/+/server/db/db.t+db.t2/c1/c1=0c2/");
    }

    @Test
    public void filters() throws SQLException {
        assertRedirected("/+/server/db/db.t/db.t.c1//filter","db.t.c1=7",    "/+/server/db/db.t/c1//c1=7/");
    }

    @Test
    public void hides() throws SQLException {
        assertRedirected("/+/server/db/db.t/db.t.c1+db.t.c2/hide","db.t.c1", "/+/server/db/db.t/c2/");
    }

    private void assertRedirected(String path, String query, String dest) throws SQLException {
        assertEquals(URIs.of(dest),DBRedirectsHandler.redirectsActionsTo(path,query));
    }
}
