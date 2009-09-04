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
public class SearchRedirectsHandlerTest {

    @Test
    public void handled() throws IOException {
        SearchRedirectsHandler   handler = new SearchRedirectsHandler();
        PageRequest   request = PageRequest.path("/");
        PageResponse response = handler.produce(request);
        assertNull(response);
    }

    @Test
    public void rootAlonePlusSearch() throws SQLException {
        assertRedirected(
            "/search","foosome", "/foosome/");
    }

    @Test
    public void nosearchAlonePlusSearch() throws SQLException {
        assertRedirected(
            "/+/search","foosome", "/foosome/");
    }

    @Test
    public void searchAlonePlusSearch() throws SQLException {
        assertRedirected(
            "/foo/search","foosome", "/foosome/");
    }

    @Test
    public void searchAndMorePlusSearch() throws SQLException {
        assertRedirected(
            "/foo/other+stuff/search","foosome", "/foosome/other+stuff/");
    }

    @Test
    public void nosearchAndMorePlusSearch() throws SQLException {
        assertRedirected(
            "/+/other+stuff/search","foosome", "/foosome/other+stuff/");
    }

    private void assertRedirected(String path, String target, String dest) throws SQLException {
        assertEquals(URIs.of(dest),SearchRedirectsHandler.redirectSearchesTo(path,target));
    }
}
